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

public class testParam extends AbstractTableData {
    private String[] columnNames;
    private int columnNum = 10;
    private int colNum = 0;
    ArrayList<Object[]> valueList = null;

    public testParam() {
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
                //获取第一个参数名称，默认配置列名参数
                String paramValue = ((ParameterProvider) this.parameters.get().toArray()[0]).getValue().toString();
                //获取第二个参数
                String paramName = ((ParameterProvider) this.parameters.get().toArray()[0]).getName();
                if (paramValue == null || "".equals(paramValue)) {
                    paramValue = paramName;
                }
                FineLoggerFactory.getLogger().debug("getColumnCount参数：------------------" + paramName + "...........值:" + paramValue);
                this.columnNames = getParam(paramValue);
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
        if (this.valueList == null) {
            //python脚本返回类型Flag
            String logFlag = "LOG";
            String valueFlag = "VALUE";
            Object[] objects = this.parameters.get().toArray();
//            传给Python脚本的参数数组
            String[] pythonParameters = new String[objects.length];
            pythonParameters[0] = "python";
            FineLoggerFactory.getLogger().debug("init：参数数量------------------" + objects.length);
            for (int i = 0; i < objects.length; i++) {
//                第二个参数默认执行脚本路径
                Object object = objects[i];
                ParameterProvider parameterProvider = (ParameterProvider) object;
                String name = parameterProvider.getName();
                String value = "".equals(parameterProvider.getValue().toString()) ? " " : parameterProvider.getValue().toString();
                FineLoggerFactory.getLogger().debug("init：----------参数名:" + name + "--------参数值:" + value+"------------i:"+i);
                if (i == 0) {
                    continue;
                }
                pythonParameters[i] = value.replaceAll("','","@");
            }
            ArrayList<Object[]> valueLists = new ArrayList<>();
            try {
//                String[] args1 = new String[]{"python", pythonPath, "2022-12-12"};
                FineLoggerFactory.getLogger().debug("运行python脚本：------------------" + pythonParameters[1] + ",传参:" + Arrays.toString(pythonParameters));
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
                this.valueList = valueLists;
                FineLoggerFactory.getLogger().error("获取返回值成功,页面渲染中");
            } catch (Exception e) {
                try {
                    throw new Exception(e.getMessage());
                } catch (Exception exception) {
                    FineLoggerFactory.getLogger().error(e.getMessage());
                }
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
                String[] args1 = new String[]{"python", "D:\\HTRB\\htrb_chart1.py", "2022-12-12"};
                Process process = Runtime.getRuntime().exec(args1);
                FineLoggerFactory.getLogger().error("获取返回值：------------------" + System.currentTimeMillis());
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
                this.valueList = valueLists;
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

    private String[] getParam(String templateName) throws Exception {
        java.sql.Connection con = getConnection();
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
        }
    }

    public java.sql.Connection getConnection() {
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


    private void printLog(String line) throws Exception{
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

    public static void main(String[] args) {
        testParam testParam = new testParam();
        testParam.test();
    }
}
