/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ballerina.stdlib.file.compiler;

import io.ballerina.projects.DiagnosticResult;
import io.ballerina.projects.Package;
import io.ballerina.projects.PackageCompilation;
import io.ballerina.projects.ProjectEnvironmentBuilder;
import io.ballerina.projects.directory.BuildProject;
import io.ballerina.projects.environment.Environment;
import io.ballerina.projects.environment.EnvironmentBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * File compiler plugin tests.
 */
public class CompilerPluginTest {

    private static final Path RESOURCE_DIRECTORY = Paths.get("src", "test", "resources", "test-src")
            .toAbsolutePath();
    private static final Path DISTRIBUTION_PATH = Paths.get("build", "target", "ballerina-distribution")
            .toAbsolutePath();

    @Test
    public void testCompilerPlugin() {
        Package currentPackage = loadPackage("package_01");
        PackageCompilation compilation = currentPackage.getCompilation();

        DiagnosticResult diagnosticResult = compilation.diagnosticResult();
        Assert.assertEquals(diagnosticResult.diagnostics().size(), 0);
    }

    @Test
    public void testCompilerPluginWithInvalidParams() {
        Package currentPackage = loadPackage("package_02");
        PackageCompilation compilation = currentPackage.getCompilation();
        String errMsg = "ERROR [file_service.bal:(29:4,31:5)] the remote function should only contain " +
                "file:FileEvent parameter";
        String errMsg1 = "ERROR [file_service.bal:(33:4,35:5)] invalid function name `onEdit`, " +
                "file listener only supports `onCreate`, `onModify` and `onDelete` remote functions";
        String errMsg2 = "ERROR [file_service.bal:(37:4,39:5)] the remote function should only contain " +
                "file:FileEvent parameter";
        DiagnosticResult diagnosticResult = compilation.diagnosticResult();
        Assert.assertEquals(diagnosticResult.diagnostics().size(), 3);
        Object[] errors = diagnosticResult.diagnostics().toArray();
        Assert.assertEquals(errors[0].toString(), errMsg);
        Assert.assertEquals(errors[1].toString(), errMsg1);
        Assert.assertEquals(errors[2].toString(), errMsg2);
    }

    @Test
    public void testCompilerPluginWithInvalidParamsType() {
        Package currentPackage = loadPackage("package_03");
        PackageCompilation compilation = currentPackage.getCompilation();
        String errMsg = "ERROR [file_service.bal:(29:4,31:5)] invalid parameter type `string` provided for remote " +
                "function. Only file:FileEvent is allowed as the parameter type";
        DiagnosticResult diagnosticResult = compilation.diagnosticResult();
        Assert.assertEquals(diagnosticResult.diagnostics().size(), 1);
        Assert.assertTrue(diagnosticResult.diagnostics().stream().anyMatch(
                diagnostic -> errMsg.equals(diagnostic.toString())));
    }

    @Test
    public void testCompilerPluginWithEmptyFunction() {
        Package currentPackage = loadPackage("package_04");
        PackageCompilation compilation = currentPackage.getCompilation();
        String errMsg = "ERROR [file_service.bal:(23:8,23:20)] at least a single remote function " +
                "required in the service";
        DiagnosticResult diagnosticResult = compilation.diagnosticResult();
        Assert.assertEquals(diagnosticResult.diagnostics().size(), 1);
        Assert.assertTrue(diagnosticResult.diagnostics().stream().anyMatch(
                diagnostic -> errMsg.equals(diagnostic.toString())));
    }

    @Test
    public void testCompilerPluginWithRemoteFunc() {
        Package currentPackage = loadPackage("package_05");
        PackageCompilation compilation = currentPackage.getCompilation();
        String errMsg = "ERROR [file_service.bal:(29:21,29:29)] invalid token 'remote'";
        String errMsg1 = "ERROR [file_service.bal:(25:4,26:5)] missing remote keyword in the remote " +
                "function `onCreate`";
        String errMsg2 = "ERROR [file_service.bal:(28:4,29:5)] missing remote keyword in the remote " +
                "function `onModify`";
        DiagnosticResult diagnosticResult = compilation.diagnosticResult();
        Assert.assertEquals(diagnosticResult.diagnostics().size(), 3);
        Object[] errors = diagnosticResult.diagnostics().toArray();
        Assert.assertEquals(errors[0].toString(), errMsg);
        Assert.assertEquals(errors[1].toString(), errMsg1);
        Assert.assertEquals(errors[2].toString(), errMsg2);
    }

    @Test
    public void testCompilerPluginWithListener() {
        Package currentPackage = loadPackage("package_06");
        PackageCompilation compilation = currentPackage.getCompilation();
        String errMsg = "ERROR [file_service.bal:(19:1,22:4)] listener variable incompatible types: 'localFolder' " +
                "is not a Listener object";
        String errMsg1 = "ERROR [file_service.bal:(19:10,19:18)] unknown type 'Listener'";
        String errMsg2 = "ERROR [file_service.bal:(19:33,22:3)] cannot infer type of the object from '(other|error)'";
        String errMsg4 = "ERROR [file_service.bal:(29:25,29:37)] invalid listener attachment";
        DiagnosticResult diagnosticResult = compilation.diagnosticResult();
        Assert.assertEquals(diagnosticResult.diagnostics().size(), 5);
        Object[] errors = diagnosticResult.diagnostics().toArray();
        Assert.assertEquals(errors[0].toString(), errMsg);
        Assert.assertEquals(errors[1].toString(), errMsg1);
        Assert.assertEquals(errors[2].toString(), errMsg2);
        Assert.assertFalse(errors[3].toString().isEmpty());
        Assert.assertEquals(errors[4].toString(), errMsg4);
    }

    @Test
    public void testCompilerPluginWithReturn() {
        Package currentPackage = loadPackage("package_07");
        String errMsg = "ERROR [file_service.bal:(25:4,27:5)] return types are not allowed in the remote " +
                "function `onCreate`";
        PackageCompilation compilation = currentPackage.getCompilation();
        DiagnosticResult diagnosticResult = compilation.diagnosticResult();
        Assert.assertEquals(diagnosticResult.diagnostics().size(), 1);
        Assert.assertTrue(diagnosticResult.diagnostics().stream().anyMatch(
                diagnostic -> errMsg.equals(diagnostic.toString())));
    }

    @Test
    public void testCompilerPluginWithDummyAndMultipleService() {
        Package currentPackage = loadPackage("package_08");
        String errMsg = "ERROR [file_service.bal:(35:4,37:5)] return types are not allowed in the remote " +
                "function `onCreate`";
        String errMsg1 = "ERROR [file_service.bal:(49:4,51:5)] return types are not allowed in the remote " +
                "function `onCreate`";
        PackageCompilation compilation = currentPackage.getCompilation();
        DiagnosticResult diagnosticResult = compilation.diagnosticResult();
        Assert.assertEquals(diagnosticResult.diagnostics().size(), 2);
        Object[] errors = diagnosticResult.diagnostics().toArray();
        Assert.assertEquals(errors[0].toString(), errMsg);
        Assert.assertEquals(errors[1].toString(), errMsg1);
    }

    private Package loadPackage(String path) {
        Path projectDirPath = RESOURCE_DIRECTORY.resolve(path);
        BuildProject project = BuildProject.load(getEnvironmentBuilder(), projectDirPath);
        return project.currentPackage();
    }

    private static ProjectEnvironmentBuilder getEnvironmentBuilder() {
        Environment environment = EnvironmentBuilder.getBuilder().setBallerinaHome(DISTRIBUTION_PATH).build();
        return ProjectEnvironmentBuilder.getBuilder(environment);
    }
}
