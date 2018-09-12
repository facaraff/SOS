package utils.powerIteration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/* 
 * Classe que faz a leitura dos arquivos.
 * O arquivo deve ser da forma:
 * 
 * n x n
 * a_0 ..... a_n
 * .
 * .
 * a_0 ..... a_n
 * 
 * */
public class ReadFile {
	
	public static double[][] readMatrixFromFile(String path) throws Exception {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(path);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String lineStr = bufferedReader.readLine();
			if (lineStr == null) {
				throw new Exception("Invalid File!");
			}
			// The matrix is put
			String[] header = lineStr.split("x");
			int linesNum = Integer.parseInt(header[0]);
			int columnsNum = Integer.parseInt(header[1]);
			double[][] matriz = new double[linesNum][columnsNum];

			for (int i = 0; i < linesNum; i++) {
				lineStr = bufferedReader.readLine();
				String[] columnsValues = lineStr.split(" ");
				for (int j = 0; j < columnsNum; j++) {
					matriz[i][j] = Double.parseDouble(columnsValues[j]);
				}
			}
			return matriz;
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("404 - File not Found: " + path);
		} catch (IOException e) {
			throw new IOException("Error reading from file", e);
		} finally {
			fileReader.close();
		}
	}

}
