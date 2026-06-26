package com.reqres.runner;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * JUnit 5 Platform Suite entry point that drives the Cucumber engine.
 * Glue and plugins are configured in {@code src/test/resources/junit-platform.properties}.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class RunCucumberTest {
}
