package proj.taskmanagementapp.dataAccess;

import java.io.*;

public class TaskSerialization {
    private static final String FILE_NAME = "data.txt"; // The file where data is stored

    public static void serialize() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            SerializedData data = new SerializedData(); // Create the wrapper object
            out.writeObject(data); // Serialize the entire data object
            System.out.println("Data successfully saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deserialize() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            SerializedData data = (SerializedData) in.readObject();
            data.restore(); // Restore the static data
            System.out.println("Data successfully loaded!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing data found, starting fresh.");
        }
    }
}
