package com.reqres.runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value =
        "pretty," +
        "html:target/cucumber-reports/cucumber.html," +
        "json:target/cucumber-reports/cucumber.json," +
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.reqres.steps")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "classpath:features")
public class CucumberRunnerTest {
}
