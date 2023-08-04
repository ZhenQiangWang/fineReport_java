package com.fr.data;

import com.fr.base.Parameter;
import com.fr.general.data.TableDataException;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ParameterProvider;

public class WzqParam extends AbstractTableData{

    private String[] columnNames;
    private int columnNum = 12;

    public WzqParam() {
        FineLoggerFactory.getLogger().error("WzqParam...");
        ParameterProvider[] parameterProviders = new ParameterProvider[2];
        parameterProviders[0] = new Parameter("tableName");
        parameterProviders[1] = new Parameter("columnNames");
        FineLoggerFactory.getLogger().error("存入默认参数...");
        setParameters(parameterProviders);
        FineLoggerFactory.getLogger().error("存入默认参数成功");
        String str = ((ParameterProvider) this.parameters.get().toArray()[0]).getValue().toString();
        FineLoggerFactory.getLogger().error("WzqParam获取参数..."+str);
        this.columnNames = new String[]{"ID号", "出货/销退单号", "批号", "型号", "子平台代码", "母平台代码", "类型", "发货数量(N)", "发货日期",
                "失效数量(n)", "失效日期", "器件小时数"};;

    }

    @Override
    public int getColumnCount() throws TableDataException {
        return 0;
    }

    @Override
    public String getColumnName(int i) throws TableDataException {
        return null;
    }

    @Override
    public int getRowCount() throws TableDataException {
        return 0;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        return null;
    }

    public String[] getColumnName(){
        return this.columnNames;
    }
}
