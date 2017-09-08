package sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig {

	/*
	<!-- CustomRedisHttpSessionConfiguration is a replica of RedissonHttpSessionConfiguration 
    with added support to remove jvmroute postfix which will be released on spring-session future version -->
<bean class="org.springframework.session.data.redis.CustomRedisHttpSessionConfiguration" p:maxInactiveIntervalInSeconds="1860" />
<bean class="org.springframework.session.web.http.DefaultCookieSerializer">
	<property name="cookieName" value="JSESSIONID"/>
	<property name="jvmRoute" value="${px_instance_id}"/>
</bean>			

<beans profile="redis-standalone">
	<!--Restricting auto subscription to session destroy events (AWS elastic cache not allowing config command)  
	http://docs.spring.io/spring-session/docs/current/reference/html5/#api-redisoperationssessionrepository-sessiondestroyedevent -->
	<util:constant static-field="org.springframework.session.data.redis.config.ConfigureRedisAction.NO_OP"/>					
	<bean class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory"
  	 p:hostName="${redis.standalone.host}" p:port="${redis.standalone.port}"/>
</beans>

*/

	@Bean
    public CookieSerializer cookieSerializer() {
            DefaultCookieSerializer serializer = new DefaultCookieSerializer();
            serializer.setCookieName("SESSION"); 
            serializer.setCookiePath("/"); 
            //serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); 
            serializer.setDomainName("pexa.local");
            serializer.setUseHttpOnlyCookie(true);
            return serializer;
    }

}
