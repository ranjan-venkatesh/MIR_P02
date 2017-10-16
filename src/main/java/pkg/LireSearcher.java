package pkg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import net.semanticmetadata.lire.builders.DocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.global.ColorLayout;
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher;
import net.semanticmetadata.lire.searchers.ImageSearchHits;
import net.semanticmetadata.lire.searchers.ImageSearcher;

/***
 * This class implements Image searching and matches it to similar images using LIRE libraries
 * @since   09-06-2017
 * @version 1.0
 */
/*
 * 
 */
public class LireSearcher {
	/***
	 * 
	 * @param indexPath  Path to index folder location
	 * @param queryPath  Path to query image
	 * @throws IOException  This method throws IOException
	 */
	public static void imageSearcher(String indexPath, String queryPath) throws IOException {

		BufferedImage queryImg = null;
		// if (queryPath !=null) {
		File f = new File(queryPath);

		queryImg = ImageIO.read(f);
		
		IndexReader ir = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
		ImageSearcher searcher = new GenericFastImageSearcher(30, ColorLayout.class);
		// ImageSearcher searcher = new GenericFastImageSearcher(30,
		// AutoColorCorrelogram.class); // for another image descriptor ...

		// searching with a image file ...
		ImageSearchHits hits = searcher.search(queryImg, ir);

		System.out.println(hits.length()+" Similar images to ["+queryPath+"] were found");
		for (int i = 0; i < hits.length(); i++) {
			String fileName = ir.document(hits.documentID(i)).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
			System.out.println("at ["+fileName+ "] \t\t\twith a score of ["+hits.score(i)+"]" );
			
		}
	}
}