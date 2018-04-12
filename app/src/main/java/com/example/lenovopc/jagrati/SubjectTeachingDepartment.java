package com.example.lenovopc.jagrati;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

public class SubjectTeachingDepartment extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_teaching_department);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            setBackOnClickListener();

            String subjectName = bundle.getString("subjectName");
            setPageTitle(subjectName);

            String subjectId = bundle.getString("subjectId");
            getTeachers(subjectId);
            final ImageButton optionBtn = (ImageButton) findViewById(R.id.options);
            optionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPopupButtonClick(optionBtn);
                }
            });
        }
    }

    protected void onPopupButtonClick(View button) {
        PopupMenu popup = new PopupMenu(this, button);
        popup.getMenuInflater().inflate(R.menu.menu_options_2, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(SubjectTeachingDepartment.this,
                        "Clicked popup menu item " + item.getTitle(),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();
    }

    private void getTeachers(String subjectId) {
        final String teachersURL = apiURL + "/department/?subject=" + subjectId;

        JsonArrayRequest req = new JsonArrayRequest(
                teachersURL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        initializeSubjectDepartment(response);
                    }
                },
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "JWT " + jwtVal);
                return headers;
            }
        };

        queue.add(req);
    }

    private void initializeSubjectDepartment(JSONArray department) {
        for (int i=0; i < department.length(); i++) {
            try {
                JSONObject volunteerSubject = department.getJSONObject(i);
                JSONObject volunteer = (JSONObject) volunteerSubject.get("volunteer");
                final String id = volunteer.getString("id");
                final String firstName = volunteer.getString("first_name");
                final String lastName = volunteer.getString("last_name");
                final String fullName = firstName + " " + lastName;
                final String discipline = volunteerSubject.getString("discipline") + " discipline";
                final String displayPictureURL = apiURL + volunteerSubject.getString("display_picture");

                LinearLayout volunteerSubjectLayout = (LinearLayout) findViewById(R.id.volunteerSubjects);
                View volunteerProfileButtonView = getLayoutInflater().inflate(R.layout.profile_subject_button, null);

                Button volunteerNameBtn = (Button) volunteerProfileButtonView.findViewById(R.id.volunteerName);
                volunteerNameBtn.setText(fullName);
                volunteerNameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent volunteerProfileActivity = new Intent("com.example.lenovopc.jagrati.PROFILE");
                        Bundle bundle = new Bundle();
                        bundle.putString("volunteerId", id);
                        volunteerProfileActivity.putExtras(bundle);
                        startActivity(volunteerProfileActivity);
                    }
                });

                TextView volunteerDisciplineText = (TextView) volunteerProfileButtonView.findViewById(R.id.volunteerDiscipline);
                volunteerDisciplineText.setText(discipline);

                NetworkImageView dpIView = (NetworkImageView) volunteerProfileButtonView.findViewById(R.id.displayPicture);
                dpIView.setImageUrl(displayPictureURL, imageLoader);

                volunteerSubjectLayout.addView(volunteerProfileButtonView);
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
