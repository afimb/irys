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
import java.util.Arrays;
import java.util.Collection;

import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.*;
import webtopo.xsd.GetTopologyVersionDocument;
import webtopo.xsd.GetTopologyVersionResponseDocument;
import webtopo.xsd.TopologyVersionType;

/**
 * Implémentation du service GetTopologyVersion
 */
public class GetTopologyVersion implements GetTopologyVersionInterface {

    @Setter private String topologyDirectory;
    @Setter private String topologyWorkingVersion;

    public GetTopologyVersion() throws SiriException {
        super();
        // initialiser ici les ressources
    }

    @SuppressWarnings("unchecked")
	public GetTopologyVersionResponseDocument getTopologyVersion(GetTopologyVersionDocument requestDoc) {
        GetTopologyVersionResponseDocument responseDoc = GetTopologyVersionResponseDocument.Factory.newInstance();
        TopologyVersionType response = responseDoc.addNewGetTopologyVersionResponse();
        File dir = new File(topologyDirectory);
        if (dir.exists()) {
            IOFileFilter filter = new AndFileFilter(DirectoryFileFilter.INSTANCE, new NotFileFilter(new NameFileFilter(topologyWorkingVersion)));
            String[] listVersion = dir.list(filter);
            if (listVersion.length == 0) {
                response.setTopologyAvailable(false);
            } else {
                Arrays.sort(listVersion);
                response.setTopologyAvailable(true);
                response.setVersion(listVersion[listVersion.length - 1]);
            }
            if (requestDoc.getGetTopologyVersion().isSetLineNameList()) {
                if (requestDoc.getGetTopologyVersion().getLineNameList()) {
                    Collection<File> list = FileUtils.listFiles(new File(dir,response.getVersion()), new String[]{"xml"}, false);
                    for (File file : list) {
                        response.addAvailableLineList(FilenameUtils.getBaseName(file.getName()));
                    }
                }
            }
        } else {
            response.setTopologyAvailable(false);
        }
        return responseDoc;

    }
}
