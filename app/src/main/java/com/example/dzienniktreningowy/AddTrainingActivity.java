import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddTrainingActivity extends AppCompatActivity {

    private EditText editName, editReps, editDuration;
    private Spinner spinnerDifficulty;
    private Button buttonSave, buttonCancel;
    private TrainingDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);

        dbHelper = new TrainingDbHelper(this);

        editName = findViewById(R.id.editExerciseName);
        editReps = findViewById(R.id.editRepsCount);
        editDuration = findViewById(R.id.editDuration);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);

        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Łatwy", "Średni", "Trudny"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);

        buttonSave.setOnClickListener(v -> saveTraining());
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void saveTraining() {
        String name = editName.getText().toString().trim();
        String repsString = editReps.getText().toString().trim();
        String durationString = editDuration.getText().toString().trim();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        
        if (name.isEmpty() || repsString.isEmpty() || durationString.isEmpty()) {
            Toast.makeText(this, "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }

        int reps = Integer.parseInt(repsString);
        int duration = Integer.parseInt(durationString);

        if (reps <= 0) {
            Toast.makeText(this, "Liczba powtórzeń musi być większa od zera", Toast.LENGTH_SHORT).show();
            return;
        }


        String currentDate = java.time.LocalDate.now().toString();


        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TrainingDbHelper.COLUMN_NAME, name);
        values.put(TrainingDbHelper.COLUMN_REPS, reps);
        values.put(TrainingDbHelper.COLUMN_DURATION, duration);
        values.put(TrainingDbHelper.COLUMN_DATE, currentDate);
        values.put(TrainingDbHelper.COLUMN_DIFFICULTY, difficulty);

        long newRowId = db.insert(TrainingDbHelper.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Błąd zapisu", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Trening zapisany", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
