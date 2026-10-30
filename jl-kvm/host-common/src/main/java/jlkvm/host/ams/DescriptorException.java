package jlkvm.host.ams;

import java.io.IOException;

/** A JAD / manifest that cannot be used (missing required attribute, bad syntax). */
public class DescriptorException extends IOException {
    public DescriptorException(String message) { super(message); }
    public DescriptorException(String message, Exception cause) { super(message, cause); }
}
