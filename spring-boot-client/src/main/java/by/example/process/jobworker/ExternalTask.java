package by.example.process.jobworker;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation class to mark all external tasks that need to be created by Spring and assigned to
 * topic
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Profile("!test")
public @interface ExternalTask {

  String topic();
}
