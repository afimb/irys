/**
 * Siri Product - Produit SIRI
 *
 * a set of tools for easy application building with respect of the France Siri
 * Local Agreement
 *
 * un ensemble d'outils facilitant la realisation d'applications respectant le
 * profil France de la norme SIRI
 *
 * Copyright DRYADE 2009-2010
 */
package irys.webtopo.server.producer;

import irys.common.SiriException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.*;
import webtopo.xsd.GetTopologyDocument;
import webtopo.xsd.GetTopologyResponseDocument;
import webtopo.xsd.LineDefinitionType;
import webtopo.xsd.TopologyType;

/**
 * Implémentation du service GetTopologyVersion
 */
public class GetTopology implements GetTopologyInterface {

    @Setter private String topologyDirectory;
    @Setter private String topologyWorkingVersion;
    @Setter private String encoding = "ISO-8859-1";

    public GetTopology() throws SiriException {
        super();
        // initialiser ici les ressources
    }

    @Override
    public GetTopologyResponseDocument getTopology(GetTopologyDocument requestDoc) {
        GetTopologyResponseDocument responseDoc = GetTopologyResponseDocument.Factory.newInstance();
        TopologyType response = responseDoc.addNewGetTopologyResponse();
        List<String> lineList = new ArrayList<String>();
        if (requestDoc.getGetTopology().sizeOfRequestedLineListArray() > 0) {
            lineList.addAll(Arrays.asList(requestDoc.getGetTopology().getRequestedLineListArray()));
        }
        String producerRef = null;
        if (requestDoc.getGetTopology().isSetProducerRef()) {
            producerRef = requestDoc.getGetTopology().getProducerRef();
        }
        if (producerRef == null) producerRef = "";

        File dir = new File(topologyDirectory);
        if (dir.exists()) {
            IOFileFilter filter = new AndFileFilter(DirectoryFileFilter.INSTANCE, new NotFileFilter(new NameFileFilter(topologyWorkingVersion)));
            String[] listVersion = dir.list(filter);
            if (listVersion.length == 0) {
                response.setTopologyAvailable(false);
            } else {
                // TODO filtrer les noms commencant par producerref si non vide.
                Arrays.sort(listVersion);
                response.setTopologyAvailable(true);
                File versionDir = new File(dir, listVersion[listVersion.length - 1]);
                response.setVersion(listVersion[listVersion.length - 1]);
                filter = new SuffixFileFilter(".xml");
                @SuppressWarnings("unchecked")
                Collection<File> list = FileUtils.listFiles(versionDir, filter, null);
                for (File file : list) {
                    try {
                        String lineName = FilenameUtils.getBaseName(file.getName());
                        if ((!lineList.isEmpty() && lineList.contains(lineName)) || producerRef.equals("") || lineName.startsWith(producerRef)) {
                            String data = FileUtils.readFileToString(file, encoding);
                            LineDefinitionType lineDefinition = response.addNewLineDefinition();
                            lineDefinition.setName(lineName);
                            lineDefinition.setNeptuneData(data);

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(GetTopology.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        } else {
            response.setTopologyAvailable(false);
        }
        return responseDoc;
    }

}
