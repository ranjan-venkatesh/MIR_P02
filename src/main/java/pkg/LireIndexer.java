package pkg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.ColorLayout;
import net.semanticmetadata.lire.imageanalysis.features.global.EdgeHistogram;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
import net.semanticmetadata.lire.imageanalysis.features.global.ScalableColor;
import net.semanticmetadata.lire.utils.FileUtils;

/***
 * This class implements Image indexing using LIRE libraries
 * @since   09-06-2017
 * @version 1.0
 */
//
public class LireIndexer {
	/***
	 * 
	 * @param imageDbPath Path to Image database
	 * @param indexDir Path to directory where Index folder would be created
	 * @return Returns path of the location where Index folder is created
	 * @throws IOException This method throws IOException
	 */
	public static String imageIndexer(String imageDbPath,String indexDir) throws IOException {
       
        //imageDbPath stores path to image database
        String indexPath = indexDir.concat("\\index");//creating an index folder at indexDir location
        
        // Getting all images from a directory and its sub directories.
        ArrayList<String> images = FileUtils.getAllImages(new File(imageDbPath), true);

        // Creating a document builder and indexing all files.
        GlobalDocumentBuilder globalDocumentBuilder= new GlobalDocumentBuilder(false);
        
        /*
            Then add those features we want to extract in a single run:
         */
        globalDocumentBuilder.addExtractor(ColorLayout.class);
        globalDocumentBuilder.addExtractor(ScalableColor.class);
        globalDocumentBuilder.addExtractor(EdgeHistogram.class);
        globalDocumentBuilder.addExtractor(CEDD.class);
        globalDocumentBuilder.addExtractor(FCTH.class);
        
                  
        // Creating an Lucene IndexWriter
        IndexWriterConfig conf = new IndexWriterConfig(new WhitespaceAnalyzer());
        IndexWriter iw = new IndexWriter(FSDirectory.open(Paths.get(indexPath)), conf);
        iw.deleteAll();
        
        // Iterating through images building the low level features
        System.out.println("\n\n\t\tIndex file location-"+indexPath);
        for (Iterator<String> it = images.iterator(); it.hasNext(); ) {
            String imageFilePath = it.next();
            System.out.println("Indexing " + Paths.get(imageFilePath).getFileName());
            try {
                BufferedImage img = ImageIO.read(new FileInputStream(imageFilePath));
                //System.out.println(img.getSampleModel().getNumBands());
                Document document = globalDocumentBuilder.createDocument(img, imageFilePath);
                iw.addDocument(document);
            } catch (Exception e) {
                System.err.println("Error occurred while processing -"+Paths.get(imageFilePath).getFileName());
                e.printStackTrace();
            }
        }
        // closing the IndexWriter
        iw.close();
        System.out.println("\n\t\t*************Indexing Completed!*************");
        return indexPath;
    }

}
