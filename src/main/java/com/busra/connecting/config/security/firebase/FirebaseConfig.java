package com.busra.connecting.config.security.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class FirebaseConfig implements DisposableBean {

    // TODO : Change this values
    private interface OPENSOURCE {
        String credential = "firebase/centrenews-dfc60-firebase-adminsdk-g0ac7-05701a1a6c.json";
        String host = "https://centrenews-dfc60.firebaseio.com";
    }

    @Bean
    public FirebaseApp website() throws IOException {
        Resource resource = new ClassPathResource(OPENSOURCE.credential);

        FirebaseOptions options =  FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                .setDatabaseUrl(OPENSOURCE.host)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Override
    public void destroy() throws Exception {
        for (FirebaseApp firebaseApp : FirebaseApp.getApps()) {
            firebaseApp.delete();
        }
    }
    @Bean
    public FirebaseParser parser() {
        return new FirebaseParser();
    }
}
