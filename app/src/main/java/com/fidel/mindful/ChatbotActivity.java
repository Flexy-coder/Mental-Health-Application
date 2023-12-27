package com.fidel.mindful;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fidel.mindful.UsageStatistics;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import com.fidel.mindful.ChatAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatbotActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessages> chatMessageList;
    private EditText editTextUserMessage;
    private Button buttonSend;
    private Toolbar toolbar;

    private final Handler handler = new Handler();
    private DatabaseReference usageStatisticsRef; // Firebase reference for usage statistics

    // List of predefined bot responses
    private List<String> customResponses;

    // Handler for delayed response
    private Handler responseHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewChat);
        editTextUserMessage = findViewById(R.id.editTextUserMessage);
        buttonSend = findViewById(R.id.buttonSend);
        toolbar = findViewById(R.id.toolbar);

        // Set the custom toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.item_therapy2);

        View customToolbarView = getSupportActionBar().getCustomView();
        ImageView profileImageView = customToolbarView.findViewById(R.id.profileImageView);
        TextView fullNameTextView = customToolbarView.findViewById(R.id.fullNameTextView);

        // Initialize the chat message list and adapter
        chatMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessageList);

        // Configure the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);

        usageStatisticsRef = FirebaseDatabase.getInstance().getReference("usage_statistics");

        // Initialize predefined bot responses
        customResponses = new ArrayList<>();
        customResponses.add("Hello! How can I assist you?");
        customResponses.add("I'm here to help. What's on your mind?");
        customResponses.add("Good morning to you too.");
        customResponses.add("Good afternoon to you too.");
        customResponses.add("Good evening to you too.");
        customResponses.add("That's good to know.");
        customResponses.add("My name is Mindy.");
        customResponses.add("Nice to meet you too.");
        customResponses.add("What makes you feel that way?");
        customResponses.add("Anxiety symptoms may include any or all the following: numbness or tingling, feeling hot, wobbliness in legs, inability to relax, having a fear of the worst happening and also the feeling of dizziness or lightheadedness. In addition to that, other symptoms may include a pounding heart, unsteadiness, feeling of fear, nervousness, choking or even difficulty in breathing. There may also be the fear of dying, feeling of indigestion, a flushed face or hot/cold sweats. If you experience any of these please visit a therapist to perform a mental health test and properly gauge the seriousness of the situation");
        customResponses.add("Post Traumatic Stress Disorder(PTSD) may be characterized by any of the following: repeated, disturbing and unwanted dreams or memories of the stressful experience. There may also be the sudden feeling as if the stressful experience were actually happening again. One might feel very upset or have very strong physical reactions when something reminds them of the stressful experience. In addition to that, there may be avoidance of any form of external reminder in the form of memory, thought or feeling to the stressful experience. In some cases, there are those who have trouble remembering important parts of the stressful experience. Others may have strong fears or negative beliefs about themselves or blame themselves for the stressful experience. In the long run, people experiencing this condition tend to lose interest in the activities they used to enjoy. Their behavior will tend to be irritable or aggressive, hypervigilant, being jumpy, difficulty in concentration or falling asleep. If you experience any of these, please visit a therapist for further assessment and counselling. ");
        customResponses.add("Depression tends to be characterized by the following symptoms: the feeling of sadness or discouragement, loss in satisfaction when accomplishing tasks.There is also the feeling of guilt, irritation, poor decision making, disappointment, self harm or even suicidal thoughts. Other symptoms include loss of sleep or tiredness, loss of appetite and weight. There is also increased worry and subsequent change in interests regarding sexual preference. If you experience any of the above please visit a therapist for additional help and counselling.");



        // Add more responses as needed.

        // Handle send button click
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    // Method to send a user message
    private void sendMessage() {
        String userMessage = editTextUserMessage.getText().toString().trim();
        if (!userMessage.isEmpty()) {
            // Add the user's message to the chat
            chatMessageList.add(new ChatMessages(userMessage, true));
            chatAdapter.notifyDataSetChanged();

            // Clear the input field
            editTextUserMessage.getText().clear();

            // Simulate a delay before selecting a predefined response
            simulateResponseDelay(userMessage);
        }
    }

    // Method to simulate a delay and then select a predefined bot response
    private void simulateResponseDelay(final String userMessage) {
        // Add a delay of 2 seconds (2000 milliseconds)
        responseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // After the delay, select a predefined response
                String botResponse = getBotResponse(userMessage);
                if (botResponse != null) {
                    // Add the chatbot's message to the chat
                    chatMessageList.add(new ChatMessages(botResponse, false));
                    chatAdapter.notifyDataSetChanged();
                }
                // Log the usage statistics
                logUsageStatistics("ChatbotActivity");
            }
        }, 2000); // 2000 milliseconds = 2 seconds
    }

    // Method to select a predefined bot response based on user input
// Inside the ChatbotActivity class...

    // Method to select a predefined bot response based on user input (case-insensitive)


    // Method to select a predefined bot response based on user input (case-insensitive)
    private String getBotResponse(String userMessage) {
        // Convert the user's message to lowercase for case-insensitive comparison
        userMessage = userMessage.toLowerCase();

        // Check if the user introduced their name
        if (userMessage.contains("hello") || userMessage.contains("hi")) {
            return customResponses.get(0);
        } else if (userMessage.contains("help")) {
            return customResponses.get(1);
        } else if (userMessage.contains("good morning")) {
            return customResponses.get(2);
        } else if (userMessage.contains("good afternoon")) {
            return customResponses.get(3);
        } else if (userMessage.contains("good evening")) {
            return customResponses.get(4);
        } else if (userMessage.contains("doing okay")  || userMessage.contains("doing fine")  || userMessage.contains("doing great") || userMessage.contains("doing good") || userMessage.contains("I'm good")) {
            return customResponses.get(5);
        } else if (userMessage.contains("who are you")  || userMessage.contains("your name")) {
            return customResponses.get(6);
        } else if (userMessage.contains("nice to meet you")) {
            return customResponses.get(7);
        } else if (userMessage.contains("anxious") || userMessage.contains("depressed") || userMessage.contains("stressed") ) {
            return customResponses.get(8);
        } else if (userMessage.contains("symptoms of anxiety")) {
            return customResponses.get(9);
        } else if (userMessage.contains("symptoms of post traumatic stress disorder") || userMessage.contains("symptoms of ptsd")) {
            return customResponses.get(10);
        } else if (userMessage.contains("symptoms of depression")) {
            return customResponses.get(11);
        } else if (userMessage.contains("my name is")) {
            // Extract the user's name from the message
            String userName = extractUserName(userMessage);
            if (userName != null) {
                // Construct a personalized greeting
                return ""+ getGreetingByTimeOfDay()+", " +userName + ". How are you doing?";
            }
        }

        // If no specific condition is met, return a default response
        return "I'm not sure how to respond to that.";
    }

    // Method to extract the user's name from the message
    private String extractUserName(String message) {
        // Split the message by spaces and find the part after "my name is"
        String[] parts = message.split("my name is");
        if (parts.length > 1) {
            return parts[1].trim();
        }
        return null;
    }

    // Method to get a greeting based on the time of day
    private String getGreetingByTimeOfDay() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String timeOfDayGreeting;
        if (hour >= 0 && hour < 12) {
            timeOfDayGreeting = "Good morning";
        } else if (hour >= 12 && hour < 18) {
            timeOfDayGreeting = "Good afternoon";
        } else {
            timeOfDayGreeting = "Good evening";
        }

        return timeOfDayGreeting;
    }

    private void logUsageStatistics(String activityName) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            long timestamp = System.currentTimeMillis(); // Get the current timestamp

            // Create a UsageStatistics object
            UsageStatistics statistics = new UsageStatistics(userId, activityName, timestamp);

            // Push the statistics to Firebase
            DatabaseReference newStatisticsRef = usageStatisticsRef.push();
            newStatisticsRef.setValue(statistics);
        }
    }
}
