package com.wzu.datasourcesystem.utils;

import com.wzu.datasourcesystem.pojo.DatabaseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    private static final String SQL = "SELECT * FROM ";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver", e);
        }
    }

    public static Integer getRow(String tableName) {
        Connection conn = getConnection();
        ResultSet resultSet = null;
        try {
            Statement sta = conn.createStatement();
            String sql = "select count(*) from " + tableName;
            resultSet = sta.executeQuery(sql);
            resultSet.next();
            int row = resultSet.getInt(1);
            System.out.println("行数:" + row);
            return row;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    LOGGER.error("close ResultSet and connection failure", e);
                }
            }
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            LOGGER.error("get connection failure", e);
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failure", e);
            }
        }
    }

    /**
     * 获取数据库下的所有表名
     */
    public static List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            DatabaseMetaData db = conn.getMetaData();
            System.out.println("conn.getCatalog() = " + conn.getCatalog());
            rs = db.getTables(conn.getCatalog(), null, null, new String[]{"TABLE"});
            while (rs.next()) {
                String tableName = (String) rs.getObject("TABLE_NAME");
                System.out.println("tabeName: " + tableName);
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
            LOGGER.error("getTableNames failure", e);
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
                LOGGER.error("close ResultSet failure", e);
            }
        }
        return tableNames;
    }

    /**
     * 获取表中所有字段名称
     *
     * @param tableName 表名
     * @return
     */
    public static List<String> getColumnNames(String tableName) {
        List<String> columnNames = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
//        String tableSql = SQL + "`"+tableName+"`";
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
            }
        } catch (SQLException e) {
            LOGGER.error("getColumnNames failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    LOGGER.error("getColumnNames close pstem and connection failure", e);
                }
            }
        }
        return columnNames;
    }

    /**
     * 获取表中所有字段类型
     *
     * @param tableName
     * @return
     */
    public static List<String> getColumnTypes(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
//        String tableSql = SQL + "`"+tableName+"`";
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                //columnTypes.add(rsmd.getColumnDisplaySize(i + 1));
                columnTypes.add(rsmd.getColumnTypeName(i + 1));
            }
        } catch (SQLException e) {
            LOGGER.error("getColumnTypes failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    LOGGER.error("getColumnTypes close pstem and connection failure", e);
                }
            }
        }
        return columnTypes;
    }

    /**
     * 获取表中字段长度
     *
     * @param tableName
     * @return
     */
    public static List<Integer> getColumnSizes(String tableName) {
        List<Integer> columnSizes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
//        String tableSql = SQL + "`"+tableName+"`";
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnSizes.add(rsmd.getColumnDisplaySize(i + 1));
            }
        } catch (SQLException e) {
            LOGGER.error("getColumnSizes failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    LOGGER.error("getColumnSizes close pstem and connection failure", e);
                }
            }
        }
        return columnSizes;
    }


    /**
     * 获取表中字段的所有注释
     *
     * @param tableName
     * @return
     */
    public static List<String> getColumnComments(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
//        String tableSql = SQL + "`"+tableName+"`";
        List<String> columnComments = new ArrayList<>();//列名注释集合
        ResultSet rs = null;
        try {
            pStemt = conn.prepareStatement(tableSql);
            rs = pStemt.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                columnComments.add(rs.getString("Comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    LOGGER.error("getColumnComments close ResultSet and connection failure", e);
                }
            }
        }
        return columnComments;
    }

    public static void index(DatabaseInfo database) {
        URL = "jdbc:" + database.getDbType() + "://"
                + database.getDbIp() + ":" + database.getPort() + "/"
                + database.getDbName() +
                "?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=utf-8";
        USERNAME = database.getUserName();
        PASSWORD = database.getPassword();
    }

    public static ArrayList<HashMap<String, String>> getInfo(String sql) {
        Connection conn = getConnection();
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            Statement statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                HashMap<String, String> data = new HashMap();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    data.put(metaData.getColumnName(i), resultSet.getString(i));

                }
                list.add(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    LOGGER.error("close ResultSet and connection failure", e);
                }
            }
        }
        return list;
    }


}
