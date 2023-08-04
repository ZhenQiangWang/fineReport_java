//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fr.data;

import com.alibaba.fastjson.JSONArray;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ParameterProvider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhenqiang.wang
 * 帆软报表程序报表
 * 1、通过读取数据库fineReport生成列名
 * 2、通过调用配置python脚本路径生成数据
 */
public class DynaxFineReport extends AbstractTableData {
    private String[] columnNames;
    private int columnNum = 10;
    private int colNum = 0;
    private ArrayList<Object[]> valueList = null;

    public DynaxFineReport() {
        /*ParameterProvider[] parameterProviders = new ParameterProvider[2];
        parameterProviders[0] = new Parameter("templateName");
        parameterProviders[1] = new Parameter("pythonPath");
        setDefaultParameters(parameterProviders);
        htrbdata
        D:\HTRB\main.py*/
    }

    @Override
    public int getColumnCount() {
        try {
            if (columnNames == null || columnNames.length == 0) {
                Object[] objects = this.parameters.get().toArray();
                for (Object object : objects) {
                    ParameterProvider parameterProvider = (ParameterProvider) object;
                    String name = parameterProvider.getName();
                    String value = parameterProvider.getValue().toString();
                    FineLoggerFactory.getLogger().debug("getColumnCount：参数数量------------------" + objects.length + "----------参数名:" + name + "--------参数值:" + value);
                }
                //获取第一个参数值，默认配置列名参数
                String paramValue = ((ParameterProvider) this.parameters.get().toArray()[0]).getValue().toString();
                //获取第一个参数名
                String paramName = ((ParameterProvider) this.parameters.get().toArray()[0]).getName();
                if (paramValue == null || "".equals(paramValue)) {
                    paramValue = paramName;
                }
                FineLoggerFactory.getLogger().debug("getColumnCount参数：------------------" + paramName + "...........值:" + paramValue);
                this.columnNames = getColumn(paramValue);
                this.columnNum = columnNames.length;
                this.colNum = columnNum;
            }
        } catch (Exception e) {
            try {
                FineLoggerFactory.getLogger().error(e.getMessage());
                throw new Exception(e.getMessage());
            } catch (Exception exception) {
                FineLoggerFactory.getLogger().error(e.getMessage());
            }
        }
        return this.columnNum;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return this.columnNames[columnIndex];
    }

    @Override
    public int getRowCount() {
        this.init();
        return this.valueList.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        this.init();
        return columnIndex >= this.colNum ? null : this.valueList.get(rowIndex)[columnIndex];
    }

    /**
     * 加载数据
     */
    private void init() {
        try {
            if (this.valueList == null) {
                Object[] objects = this.parameters.get().toArray();
                //传给Python脚本的参数数组
                String[] pythonParameters = new String[objects.length];
                pythonParameters[0] = "python";
                FineLoggerFactory.getLogger().debug("init：参数数量------------------" + objects.length);
                for (int i = 0; i < objects.length; i++) {
                    //第二个参数默认执行脚本路径
                    Object object = objects[i];
                    ParameterProvider parameterProvider = (ParameterProvider) object;
                    String name = parameterProvider.getName();
                    String value = "".equals(parameterProvider.getValue().toString()) ? " " : parameterProvider.getValue().toString();
                    FineLoggerFactory.getLogger().debug("init：----------参数名:" + name + "--------参数值:" + value + "------------i:" + i);
                    if (i == 0) {
                        continue;
                    }
                    pythonParameters[i] = value.replaceAll("','", "@");
                }
                this.valueList = getScriptReturnValue(pythonParameters);
                FineLoggerFactory.getLogger().error("获取返回值成功,页面渲染中");
            }
        } catch (Exception e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception exception) {
                FineLoggerFactory.getLogger().error(e.getMessage());
            }
        }
    }

    private void test() {
        if (this.valueList == null) {
            //python脚本返回类型Flag
            String logFlag = "LOG";
            String valueFlag = "VALUE";
            ArrayList<Object[]> valueLists = new ArrayList<>();
            try {
                String[] args1 = new String[]{"python", "D:\\HTRB\\htrb_chart1.py", "2023-06-13"};
                valueLists = getScriptReturnValue(args1);
                FineLoggerFactory.getLogger().error("获取返回值成功,页面渲染中：------------------" + System.currentTimeMillis());
            } catch (Exception e) {
                try {
                    throw new Exception(e.getMessage());
                } catch (Exception exception) {
                    FineLoggerFactory.getLogger().error(e.getMessage());
                }
            }
        }
    }

    /**
     * 获取当前数据集查询列
     * @param templateName 数据库中主键
     * @return 列明数组
     */
    private String[] getColumn(String templateName) {
        FineLoggerFactory.getLogger().info("获取列名：------------------" + templateName);
        String[] pythonParameters = new String[3];
        pythonParameters[0] = "python";
        pythonParameters[1] = "D:\\HTRB\\getColumn.py";
        pythonParameters[2] = templateName;
        ArrayList<Object[]> scriptReturnValue = getScriptReturnValue(pythonParameters);
        String[] columns = new String[scriptReturnValue.size()];
        for (int i = 0; i < scriptReturnValue.size(); i++) {
            columns[i] = String.valueOf(scriptReturnValue.get(i)[0]);
        }
        return columns;

        /*Connection con = getConnection();
        try {
            ArrayList<String> strings = new ArrayList<>();
            int index = 1;
            Statement stmt = con.createStatement();
            String sql = "Select COLUMN_NAME from finereport where TEMPLATE_NAME = '" +
                    templateName +
                    "' ORDER BY ID";
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            this.colNum = rsmd.getColumnCount();
            while (rs.next()) {
                strings.add(String.valueOf(rs.getString(index)));
            }
            return strings.toArray(new String[0]);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (!con.isClosed()) {
                con.close();
            }
        }*/
    }

    public Connection getConnection() {
        String driverName = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@192.168.68.60:1521:orcl";
        String username = "nx_mes";
        String password = "nx_mes";
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception exception) {
                FineLoggerFactory.getLogger().error(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public void release() throws Exception {
        super.release();
        this.valueList = null;
    }

    //    日志打印
    private void printLog(String line) throws Exception {
        String level = (line.split(":")[0]).split("@")[1].trim().toUpperCase();
        switch (level) {
            case "ERROR":
                FineLoggerFactory.getLogger().error(line);
                throw new Exception(line);
            case "INFO":
                FineLoggerFactory.getLogger().info(line);
                break;
            case "DEBUG":
                FineLoggerFactory.getLogger().debug(line);
                break;
            default:
                String msg = "脚本返回log语句未能识别..." + line;
                FineLoggerFactory.getLogger().error(msg);
                throw new Exception(msg);
        }

    }

    /**
     * 运行指定路径脚本并获取返回值
     *
     * @param pythonParameters 传递参数
     * @return 脚本执行返回值
     */
    private ArrayList<Object[]> getScriptReturnValue(String[] pythonParameters) {
        ArrayList<Object[]> valueLists = new ArrayList<>();
        try {
            //python脚本返回类型Flag
            String logFlag = "LOG";
            String valueFlag = "VALUE";
            FineLoggerFactory.getLogger().info("运行python脚本：------------------" + pythonParameters[1] + ",传参:" + Arrays.toString(pythonParameters));
            Process process = Runtime.getRuntime().exec(pythonParameters);
            FineLoggerFactory.getLogger().debug("获取返回值成功");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("GBK")));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(logFlag)) {
                    //返回值是否为LOG打印信息
                    printLog(line);
                } else if (line.startsWith(valueFlag)) {
                    String valueStr = line.split("@")[1];
                    List<String[]> values = JSONArray.parseArray(valueStr, String[].class);
                    Object[] objArray;
                    for (String[] value : values) {
                        objArray = new Object[value.length];
                        for (int i = 0; i < value.length; i++) {
                            if (value[i] == null) {
                                objArray[i] = "";
                            } else {
                                objArray[i] = value[i];
                            }

                        }
                        valueLists.add(objArray);
                    }
                }
            }
            reader.close();
            process.destroy();
        } catch (Exception e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception exception) {
                FineLoggerFactory.getLogger().error(e.getMessage());
            }
        }
        return valueLists;
    }

    public static void main(String[] args) {
        DynaxFineReport testParam = new DynaxFineReport();
        try {
//            testParam.test();
            testParam.getColumn("htrb_chart1");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        testParam.test();
    }
}
