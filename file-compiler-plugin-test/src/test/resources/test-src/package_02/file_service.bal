// Copyright (c) 2021 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/file;

listener file:Listener localFolder = new ({
    path: "src/test/resources",
    recursive: false
});

boolean createInvoke = false;
boolean modifyInvoke = false;
boolean deleteInvoke = false;

service "filesystem" on localFolder {

    remote function onCreate(file:FileEvent m, string name) {
        createInvoke = true;
    }

    remote function onEdit(file:FileEvent m) {
        modifyInvoke = true;
    }

    remote function onDelete() {
        deleteInvoke = true;
    }
}
