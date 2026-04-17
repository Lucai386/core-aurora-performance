package com.core_aurora_performance.tenant;

/**
 * ThreadLocal holder per il tenant corrente (codice ISTAT del comune).
 * Viene popolato da {@link TenantFilter} ad ogni richiesta HTTP e
 * pulito al termine della stessa.
 */
public final class TenantContext {

    private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    private TenantContext() {}

    public static void set(String codiceIstat) {
        CURRENT.set(codiceIstat);
    }

    /** Restituisce il codice ISTAT del tenant corrente, o {@code null} se non impostato. */
    public static String get() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }

    /** Lancia un'eccezione se il tenant non è impostato (utile nei metodi che lo richiedono). */
    public static String require() {
        String t = CURRENT.get();
        if (t == null || t.isBlank()) {
            throw new IllegalStateException("Tenant (X-Tenant-Id) non impostato nella richiesta corrente");
        }
        return t;
    }
}
