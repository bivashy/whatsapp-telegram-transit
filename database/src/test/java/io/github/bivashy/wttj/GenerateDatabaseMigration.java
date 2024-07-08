package io.github.bivashy.wttj;

import io.ebean.annotation.Platform;
import io.ebean.dbmigration.DbMigration;

import java.io.IOException;

public class GenerateDatabaseMigration {

    public static void main(String[] args) throws IOException {
        DbMigration dbMigration = DbMigration.create();
        dbMigration.setPlatform(Platform.POSTGRES);
        dbMigration.setPathToResources("database/src/main/resources");
        dbMigration.generateMigration();
    }

}
