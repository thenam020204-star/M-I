package jlkvm.host.net;

import java.io.IOException;
import java.util.List;

/** The real HTTP of the host (T26b): performs one request and returns the whole response. Called off the vm-thread. */
public interface HttpTransport {
    final class Response {
        public final int status;
        public final String message;
        /** Header fields in order: {key, value} (the status line is not included). */
        public final List<String[]> headers;
        public final byte[] body;

        public Response(int status, String message, List<String[]> headers, byte[] body) {
            this.status = status; this.message = message; this.headers = headers; this.body = body;
        }
    }

    /** Redirects are NOT followed. @param headers {key, value} pairs set by the application @throws IOException any network failure */
    Response execute(String method, String url, List<String[]> headers, byte[] body, int timeoutMs) throws IOException;
}
