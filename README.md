# HttpAPIGenerator
Automatically generating controller(HTTP API) for service

#### spring配置
1. 自动扫描该工程所有包
2. 如果对service类所在包名有其他配置需求，对GeneratorConfig类需要持有的属性进行单独配置

#### 端口配置
通过application.properties的server.port进行配置