module com.financex.financex_ledger {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires java.sql;


    opens com.financex.financex_ledger to javafx.fxml;
    exports com.financex.financex_ledger;
}