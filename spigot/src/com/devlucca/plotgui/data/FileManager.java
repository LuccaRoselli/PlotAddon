package com.devlucca.plotgui.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileManager {
	
	public static void save(Object obj, File file){
		try {
			if (!file.exists())
				file.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(obj);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Object load(File file){
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Object returnable = ois.readObject();
			ois.close();
			return returnable;
		} catch (Exception e) {
			return null;
		}
	}

}
