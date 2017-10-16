package pkg;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

/***
 * This is project for Indexing and Searching images using LIRE(Lucene Image
 * Retrieval)
 * 
 * @since 09-06-2017
 * @version 1.0
 */
public class LireProject {

	/***
	 * Usage: java -jar MIR_P02.jar
	 * [image_data_base_path][index_path][query_file_path]
	 * 
	 * @param args
	 *            [image_data_base_path][index_path][query_file_path]
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String imgDbPath = null;// path_to_image_folder
		String indexPath = null;// path_to_index_folder
		String queryPath = null;// path_to_query_image
		String indexDir = null;// path_to_dir_where_index_would_be_created
		String usage = " Usage:\n java -jar MIR_P02.jar [image_data_base_path][index_path][query_file_path]"
				+ "<<Note: A folder named-index would be created at given [index_path]>>";

		imgDbPath = args[0];
		indexDir = args[1];
		queryPath = args[2];

		// indexPath = indexPath.concat("\\index");

		if (args.length < 3) // checking if all 3 arguments are passed; if not
								// print usage instruction
		{
			System.out.println(usage);
			return;
		} else {
			if (!Files.exists(Paths.get(imgDbPath), LinkOption.NOFOLLOW_LINKS)) {
				System.out.println("Given Path to Image Database does not exist.\n");
				System.out.println(usage);
				return;
			}
			if (!Files.exists(Paths.get(indexDir), LinkOption.NOFOLLOW_LINKS)) {
				System.out.println("Given Path for index creation does not exist.\n");
				System.out.println(usage);
				return;
			}
			if (!Files.exists(Paths.get(queryPath), LinkOption.NOFOLLOW_LINKS)) {
				System.out.println("Given Path for query image does not exist.\n");
				System.out.println(usage);
				return;
			}

			else {
				System.out.print("Image files, Index location are located and Query Image has be captured");
				// Now give call to Indexing and Searching

				/**
				 * Indexing images
				 */
				try {
					indexPath = LireIndexer.imageIndexer(imgDbPath, indexDir);
					;

					/**
					 * Search for a query image
					 */

					LireSearcher.imageSearcher(indexPath, queryPath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
