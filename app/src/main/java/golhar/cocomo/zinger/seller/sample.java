package golhar.cocomo.zinger.seller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*
public class GPACalculatorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa_calculator);

        new getDepartmentSubjects().execute();
    }

    public class getDepartmentSubjects extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("subjects.json")));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    sb.append(line + "\n");

                JSONArray arr = new JSONArray(sb.toString());
                JSONObject obj1 = (JSONObject) arr.get(dept);
                JSONArray arr1 = obj1.getJSONArray("sem");
                JSONObject obj2 = (JSONObject) arr1.get(semester);

                semesterSubjects = new Gson().fromJson(obj2.toString(), DepartmentSubjects.SemesterSubjects.class);
                if (semesterSubjects != null)
                    subjects = semesterSubjects.getSubjects();

                adapter = new SubjectsAdapter(getApplicationContext(), subjects);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
*/