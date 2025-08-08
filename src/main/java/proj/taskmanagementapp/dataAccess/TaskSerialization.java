package proj.taskmanagementapp.dataAccess;

import java.io.*;

public class TaskSerialization {
    private static final String FILE_NAME = "data.txt";

    public TaskSerialization() {
    }

    public static void serialize() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.txt"));

            try {
                SerializedData data = new SerializedData();
                out.writeObject(data);
                System.out.println("Data successfully saved!");
            } catch (Throwable var4) {
                try {
                    out.close();
                } catch (Throwable var3) {
                    var4.addSuppressed(var3);
                }

                throw var4;
            }

            out.close();
        } catch (IOException var5) {
            IOException e = var5;
            e.printStackTrace();
        }

    }

    public static void deserialize() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.txt"));

            try {
                SerializedData data = (SerializedData)in.readObject();
                data.restore();
                System.out.println("Data successfully loaded!");
            } catch (Throwable var4) {
                try {
                    in.close();
                } catch (Throwable var3) {
                    var4.addSuppressed(var3);
                }

                throw var4;
            }

            in.close();
        } catch (ClassNotFoundException | IOException var5) {
            System.out.println("No existing data found, starting fresh.");
        }

    }
}
