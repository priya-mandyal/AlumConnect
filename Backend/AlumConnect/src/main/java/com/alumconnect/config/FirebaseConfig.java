package com.alumconnect.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    /**
     * Initializes the FirebaseApp with the provided service account credentials.
     */
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream serviceAccount =
                    FirebaseConfig.class.getResourceAsStream("/alumConnectServiceKey.json");

            assert serviceAccount != null;
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }


    /**
     * Initializes the Google Cloud Storage service with the provided service account credentials.
     */
    @Bean
    public Storage storage() throws IOException {
        InputStream serviceAccount =
                FirebaseConfig.class.getResourceAsStream("/alumConnectServiceKey.json");
        assert serviceAccount != null;
        StorageOptions options = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        return options.getService();
    }
}