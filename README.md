# dc_doctorstation
实验室项目__BS住院医生站，BS_门诊药师站

2019.08.20
1.application.properties配置文件中新增bz_flag；
2.spring-mvc.xml中添加
<mvc:annotation-driven >
  <!-- 消息转换器 -->
  <mvc:message-converters register-defaults="true">
    <bean class="org.springframework.http.converter.StringHttpMessageConverter">
      <property name="supportedMediaTypes" value="text/html;charset=UTF-8"/>
    </bean>
  </mvc:message-converters>
</mvc:annotation-driven>
解决后xmlhttp.responseText中文乱码的问题；
