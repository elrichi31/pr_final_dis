module com.example.pr_final_dis {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.pr_final_dis to javafx.fxml;
    exports com.example.pr_final_dis;
}