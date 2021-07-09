package net.cactusthorn.config.compiler;

import java.io.IOException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

public interface ClassesGenerator {

    void init(ProcessingEnvironment processingEnv);

    void generate(RoundEnvironment roundEnv, ProcessingEnvironment processingEnv) throws ProcessorException, IOException;
}
