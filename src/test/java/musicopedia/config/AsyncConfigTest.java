package musicopedia.config;

import org.junit.jupiter.api.Test;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;

class AsyncConfigTest {

    @Test
    void testTaskExecutorBean() {
        AsyncConfig config = new AsyncConfig();
        Executor executor = config.taskExecutor();
        assertNotNull(executor);
        assertTrue(executor instanceof ThreadPoolTaskExecutor);
    }

    @Test
    void testArtistProcessingExecutorBean() {
        AsyncConfig config = new AsyncConfig();
        Executor executor = config.artistProcessingExecutor();
        assertNotNull(executor);
        assertTrue(executor instanceof ThreadPoolTaskExecutor);
    }

    @Test
    void testMemberProcessingExecutorBean() {
        AsyncConfig config = new AsyncConfig();
        Executor executor = config.memberProcessingExecutor();
        assertNotNull(executor);
        assertTrue(executor instanceof ThreadPoolTaskExecutor);
    }

    @Test
    void testGetAsyncExecutor() {
        AsyncConfig config = new AsyncConfig();
        Executor executor = config.getAsyncExecutor();
        assertNotNull(executor);
        assertTrue(executor instanceof ThreadPoolTaskExecutor);
    }

    @Test
    void testGetAsyncUncaughtExceptionHandler() {
        AsyncConfig config = new AsyncConfig();
        AsyncUncaughtExceptionHandler handler = config.getAsyncUncaughtExceptionHandler();
        assertNotNull(handler);
    }
}