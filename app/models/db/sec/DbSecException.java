package models.db.sec;

public class DbSecException extends Exception {
    private String code;

    public DbSecException(
        String message
    ) {
        super( message );
        this.code = null;
    }

    public DbSecException(
        String code,
        String message
    ) {
        super( message );
        this.code = code;
    }

    public DbSecException(
        String code,
        String message,
        Throwable cause
    ) {
        super( message );
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode( String code ) {
        this.code = code;
    }
}
