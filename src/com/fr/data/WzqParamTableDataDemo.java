package com.fr.data;


import com.alibaba.fastjson.*;
import com.fr.base.Parameter;
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
 */
public class WzqParamTableDataDemo extends SimpleTableData {

    public WzqParamTableDataDemo(){
        ParameterProvider[] parameterProviders = new ParameterProvider[1];
        parameterProviders[0] = new Parameter("tableName");
        FineLoggerFactory.getLogger().error("存入默认参数...");
        this.setParameters(parameterProviders);
        FineLoggerFactory.getLogger().error("存入默认参数成功");
    }

    @Override
    public String[] initColumnNames() {
        FineLoggerFactory.getLogger().error("initColumnNames...");
        WzqParam wzqParam = new WzqParam();
        return wzqParam.getColumnName();
//        String pythonPath = ((ParameterProvider) this.parameters.get().toArray()[0]).getValue().toString();
//        FineLoggerFactory.getLogger().error("获取列..."+pythonPath);
//        return getParam();
       /* FineLoggerFactory.getLogger().error("initColumnNames..."+Arrays.toString(columnNames));
        ParameterProvider[] parameterProviders = new ParameterProvider[2];
        parameterProviders[0] = new Parameter("tableName");
        parameterProviders[1] = new Parameter("columnNames");
        FineLoggerFactory.getLogger().error("存入默认参数...");
        this.setDefaultParameters(parameterProviders);
        FineLoggerFactory.getLogger().error("存入默认参数成功");
        FineLoggerFactory.getLogger().error("获取列参数...");
        String pythonPath = ((ParameterProvider) this.parameters.get().toArray()[0]).getValue().toString();
        FineLoggerFactory.getLogger().error("获取列..."+pythonPath);*/
       /* return new String[]{"ID号", "出货/销退单号", "批号", "型号", "子平台代码", "母平台代码", "类型", "发货数量(N)", "发货日期",
                "失效数量(n)", "失效日期", "器件小时数"};*/
//        return columnNames;
    }

    @Override
    public List<Object[]> loadData() {
        FineLoggerFactory.getLogger().error("加载数据......");
        return runPython();
    }

    private ArrayList<Object[]> runPython() {
        String logFlag = "LOG";
        String paramFlag = "PARAM";
        String valueFlag = "VALUE";
//        String pythonPath = ((ParameterProvider) this.parameters.get().toArray()[1]).getValue().toString();
//        FineLoggerFactory.getLogger().info("运行Python脚本获取数据，路径："+pythonPath+".....");
        ArrayList<Object[]> valueLists = new ArrayList<>();
        try {
            String[] args1 = new String[]{"python", "D:\\HTRB\\main.py", "a", "b"};
            Process process = Runtime.getRuntime().exec(args1);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("GBK")));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(logFlag)) {
                    //返回值是否为LOG打印信息
                    printLog(line);
                }else if(line.startsWith(paramFlag)){
                    //参数数组
                    String paramStr = line.split("@")[1];
                    JSONArray parse = (JSONArray) JSON.parse(paramStr);
                    String[] strings = new String[parse.size()];
                    String[] params = parse.toArray(strings);
                }else if(line.startsWith(valueFlag)){
                    String valueStr = line.split("@")[1];
                    List<String[]> values = JSONArray.parseArray(valueStr, String[].class);
                    Object[] objArray = null;
                    for (String[] value : values){
                        objArray = new Object[value.length];
                        for (int i = 0; i < value.length; i++) {
                            if(value[i] == null){
                                objArray[i] = "";
                            }else {
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
            FineLoggerFactory.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return valueLists;
    }

    private String[] getParam(String templateName) throws SQLException {
        int index = 1;
        java.sql.Connection con = getConnection();
        Statement stmt = con.createStatement();
        String sql = "Select COLUMN_NAME from finereport where TEMPLATE_NAME = '" +
                templateName +
                "'";
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<String> strings = new ArrayList<>();
        while(rs.next()) {
            strings.add(String.valueOf(rs.getString(index)));
        }
        return strings.toArray(new String[0]);
    }


    public java.sql.Connection getConnection() {
        String driverName = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@192.168.68.60:1521:orcl";
        String username = "nx_mes";
        String password = "nx_mes";

        try {
            Class.forName(driverName);
            java.sql.Connection con = DriverManager.getConnection(url, username, password);
            return con;
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    private void printLog(String line) {
        String level = (line.split(":")[0]).split("\\.")[1].trim().toUpperCase();
        switch (level) {
            case "ERROR":
                FineLoggerFactory.getLogger().error(line);
                break;
            case "INFO":
                FineLoggerFactory.getLogger().info(line);
                break;
            case "DEBUG":
                FineLoggerFactory.getLogger().debug(line);
                break;
            default:
                FineLoggerFactory.getLogger().error("脚本返回log语句未能识别..."+line);
                break;
        }

    }

    public static void main(String[] args) {
        WzqParamTableDataDemo wzqParamTableDataDemo = new WzqParamTableDataDemo();
        ArrayList<Object[]> valueLists = wzqParamTableDataDemo.runPython();
        System.out.println("END...");
    }

}
