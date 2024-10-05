package logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Утилитный класс для получения экземпляров логгеров.
 * Использует SLF4J для создания логгеров, что позволяет
 * легко переключаться между реализациями логирования.
 */
public class LoggerFactoryUtil {
    /**
     * Возвращает логгер для указанного класса.
     * @param clazz класс, для которого требуется логгер
     * @return экземпляр логгера для указанного класса
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
