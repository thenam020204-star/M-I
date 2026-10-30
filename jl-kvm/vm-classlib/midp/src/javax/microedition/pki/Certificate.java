/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.pki;

/** MIDP 2.0 / GCF interface; the handler (if any) is jlkvm.internal.io.&lt;scheme&gt;.Protocol. */
public interface Certificate {
    String getIssuer();

    long getNotAfter();

    long getNotBefore();

    String getSerialNumber();

    String getSigAlgName();

    String getSubject();

    String getType();

    String getVersion();
}
