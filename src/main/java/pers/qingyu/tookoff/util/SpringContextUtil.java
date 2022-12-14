package pers.qingyu.tookoff.util;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * Spring 上下文获取
 * @author junlinag.li
 * @date 2021/7/13
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext context = null;

	private SpringContextUtil() {
		super();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		context = applicationContext;
	}

	/**
	 * 根据名称获取bean
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	/**
	 * 根据bean名称获取指定类型bean
	 * @param beanName bean名称
	 * @param clazz 返回的bean类型,若类型不匹配,将抛出异常
	 */
	public static <T> T getBean(String beanName, Class<T> clazz) {
		return context.getBean(beanName, clazz);
	}

	/**
	 * 根据类型获取bean
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		T t = null;
		Map<String, T> map = context.getBeansOfType(clazz);
		for (Map.Entry<String, T> entry : map.entrySet()) {
			t = entry.getValue();
			if (t != null){
				break;
			}
		}
		return t;
	}

	/**
	 * 是否包含bean
	 * @param beanName
	 * @return
	 */
	public static boolean containsBean(String beanName) {
		return context.containsBean(beanName);
	}

	/**
	 * 是否是单例
	 * @param beanName
	 * @return
	 */
	public static boolean isSingleton(String beanName) {
		return context.isSingleton(beanName);
	}

	/**
	 * bean的类型
	 * @param beanName
	 * @return
	 */
	public static Class<?> getType(String beanName) {
		return context.getType(beanName);
	}

	/**
	 * <H2>发布事件</H2>
	 *
	 * @author zhouxin
	 * @since 2022/8/16 14:58
	 * @param: event
	 @return: void
	 **/
	public static void publishEvent(ApplicationEvent event){
		context.publishEvent(event);
	}

	/**
	 * <H2>根据注解获取bean</H2>
	 *
	 * @author zhouxin
	 * @since 2022/8/16 17:58
	 * @param: annotationType
	 * @return: java.util.Map<java.lang.String,java.lang.Object>
	 **/
	public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType){
		return context.getBeansWithAnnotation(annotationType);
	}

}
