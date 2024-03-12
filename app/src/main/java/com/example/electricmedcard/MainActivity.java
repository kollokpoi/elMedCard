package com.example.electricmedcard;

import static com.example.electricmedcard.MainActivity.SqlSettings.getConnectionString;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity{
    public enum Types{
        events,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QueryTask task = new QueryTask(new iQuery() {
            @Override
            public void returner(ResultSet result) {
                try {
                    while (result.next()) {
                        String name = result.getString(2);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        task.execute("Select * from MedicalPlaces");
    }

    private interface iQuery{
        void returner(ResultSet result);
    }

    public class QueryTask extends AsyncTask<String, Void, ResultSet> {

        iQuery iQuery;
        public QueryTask(iQuery iQuery){
            this.iQuery = iQuery;
        }
        @Override
        protected ResultSet doInBackground(String... query) {

            JSONArray resultSet = new JSONArray();
            try {
                Connection con = DriverManager.getConnection(getConnectionString());

                if (con != null) {
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(query[0]);
                    return rs;
                }
            } catch (SQLException ex) {
                Log.w("SQLException error: ", ex.getMessage());
            } catch (Exception ex) {
                Log.w("Exception error: ", ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(ResultSet result) {
            iQuery.returner(result);
        }

    }

    public static class SqlSettings{
        static String instanceName = "student.permaviat.ru";
        static String db = "base2_ISP_21_2_23";
        static String username = "ISP_21_2_23";
        static String password = "3frQxZ83o#";
        static String connectionUrl = "jdbc:jtds:sqlserver://%1$s;databaseName=%2$s;user=%3$s;password=%4$s;Encrypt=false;trusted_connection=false";

        public static String  getConnectionString(){
            return String.format(connectionUrl, instanceName, db, username, password);
        }
    }
}