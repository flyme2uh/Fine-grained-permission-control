package com.bylz.fgaTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@SpringBootTest
class FineGrainedAuthenticationApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test1() throws FileNotFoundException {
        File file = new File("src/test/java/resources/authYml");
        File[] files = file.listFiles();
        for (File ymlFile : files) {
            FileInputStream inputStream = new FileInputStream(ymlFile);
            Yaml yml = new Yaml();
            Map load = yml.load(inputStream);
            HashSet<String> paths = (HashSet<String>) load.get("paths");
            System.out.println(paths);
            System.out.println(load);
        }
    }

    @Test
    public void test2() throws FileNotFoundException {
        File file = new File("src/test/java/resources/preEnhancement.yml");
        FileInputStream inputStream = new FileInputStream(file);
        Yaml yaml = new Yaml();
        Map load = yaml.load(inputStream);
        System.out.println(load);
    }
}
