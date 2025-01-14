/*
 * Copyright (c) 2020 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

module io.ballerina.stdlib.file {
    requires io.ballerina.runtime;
    requires io.ballerina.lang;
    requires io.ballerina.tools.api;
    requires io.ballerina.stdlib.time;
    requires org.slf4j;
    requires org.wso2.transport.local.file.system;
    exports org.ballerinalang.stdlib.file.nativeimpl;
    exports org.ballerinalang.stdlib.file.service;
    exports org.ballerinalang.stdlib.file.service.endpoint;
    exports org.ballerinalang.stdlib.file.utils;
}
