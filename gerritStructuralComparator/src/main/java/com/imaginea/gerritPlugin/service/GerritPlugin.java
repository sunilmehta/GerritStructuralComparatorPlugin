package com.imaginea.gerritPlugin.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.google.gerrit.extensions.annotations.Export;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.patch.PatchListCache;
import com.google.gerrit.server.project.ChangeControl;
import com.google.gson.Gson;
import com.google.gwtorm.server.SchemaFactory;
import com.google.inject.Inject;
import com.imaginea.gerritPlugin.model.ChangeDetails;
import com.imaginea.gerritPlugin.model.ChangeID;
import com.imaginea.gerritPlugin.utils.DraftUtil;
import com.imaginea.gerritPlugin.utils.FileDataRetrivalService;
import com.imaginea.javaStructuralComparator.domain.ComparisonResult;
import com.imaginea.javaStructuralComparator.domain.DraftMessage;
import com.imaginea.javaStructuralComparator.repo.ComparatorImpl;


@Export("/gerritPlugin")
public class GerritPlugin extends HttpServlet {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(GerritPlugin.class);
	
	private static final long serialVersionUID = 1L;
	private final PatchListCache patchListCache;
	private final SchemaFactory<ReviewDb> dbFactory;
	private final ChangeControl.Factory changeControlFactory;
	
	private PrintWriter out = null;
	
	@Inject
	public GerritPlugin(SchemaFactory<ReviewDb> dbFactory,
			final PatchListCache patchListCache,
			ChangeControl.Factory changeControlFactory ) {
		this.dbFactory = dbFactory;
		this.patchListCache = patchListCache;
		this.changeControlFactory = changeControlFactory;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String baseUrl = "";
		
		String comparisonResult = null;
		
		PropertyConfigurator.configure( getFileLocation("log4jPlugins.properties") );
		
		out = resp.getWriter();
		
		try {
			
			String patchUrl = req.getParameter("url");
			String patchSetUrl1 = req.getParameter("patchSetURL1");
			String patchSetUrl2 = req.getParameter("patchSetURL2");
			
			log.debug("patchUrl "+patchUrl);
			log.debug("patchSetUrl1 "+patchSetUrl1);
			log.debug("patchSetUrl2 "+patchSetUrl2);
			
			if ( null != patchUrl )
				baseUrl = patchUrl.substring(0, patchUrl.length()-1).concat("1");

			if (null != patchUrl && null != baseUrl) {
				comparisonResult = compareFile(baseUrl, patchUrl);
				if( null != comparisonResult){
					out.write( comparisonResult );
				}
			} else if( null != patchSetUrl1 && null != patchSetUrl2 ){
				comparisonResult = compareFile(patchSetUrl1, patchSetUrl2);
				if( null != comparisonResult){
					out.write( comparisonResult );
				}
			} else{
				String reqHostName = req.getServerName() + ":" + req.getServerPort();
				ChangeDetails changeDetail = new ChangeDetails();
				GerritChangeProcessingService serviceObj = new GerritChangeProcessingService();
				List<ChangeID> changeIdList = serviceObj.prepareChangeIdList(dbFactory, patchListCache, reqHostName);
				String reqChangeId = req.getParameter("id");
				log.debug("Request Parameter "+reqChangeId);
				if( null != reqChangeId ){
					changeDetail.setChangeIDs(null);
					changeDetail.setChange( serviceObj.fetchChangesbyChangeId(dbFactory, patchListCache, reqHostName, reqChangeId) );
				}else if( changeIdList.size() > 0){
					changeDetail.setChangeIDs(changeIdList);
					changeDetail.setChange( null );
				}
				
				Gson gson = new Gson();
				String changeDetailJSON = gson.toJson(changeDetail);
				log.debug("changeDetailJSON "+changeDetailJSON);
				out.write(changeDetailJSON);
			}
			log.debug("Structural Comparator Result \n:: " + comparisonResult );
		} catch (Exception e) {
			log.error("Unknown Exception ", e);
			out.write("Exceptions being thrown");
		} finally {
			out.close();
		}
	}
	
	
	private URL getFileLocation( String fileName ){
		try {
			Class cls = Class.forName("com.imaginea.gerritPlugin.service.GerritPlugin");
			ClassLoader cLoader = cls.getClassLoader();
			URL url = cLoader.getResource( fileName );
			return url;
		} catch (ClassNotFoundException e) {
			log.error("ClassNotFound ", e);
		}
		return null;
	}
	
	
	private String compareFile(String baseUrl, String patchUrl) {
		String baseFile = "";
		String patchFile = "";
		try {
			patchFile = FileDataRetrivalService.getFileDataStream(patchUrl);
			baseFile = FileDataRetrivalService.getFileDataStream(baseUrl);
		} catch ( MalformedURLException e ) {
			log.error("Incorrect Url ", e);
		} catch ( Exception e ) {
			baseFile = "";
			if( null != out){
				out.write("JavaCode \n"+patchFile);
			}
			log.error( "Exception during file Retrieval ", e);
			return null;
		}

		ComparatorImpl comparatorImpl = new ComparatorImpl();
		ComparisonResult compareResult = null;
		try{
			compareResult = comparatorImpl.compare( baseFile, patchFile );
			List<DraftMessage> draftMessage = DraftUtil.loadDraftMessage(dbFactory, changeControlFactory, patchUrl);
			
			Collections.sort(draftMessage, new Comparator<DraftMessage>() {
				@Override
				public int compare(DraftMessage o1, DraftMessage o2) {
					return Integer.valueOf(o1.getLine()).compareTo(Integer.valueOf(o2.getLine()));
				}
			});
			
			compareResult.setDraftMessage(draftMessage);
			log.debug("draftMessage Size:: "+draftMessage.size());
		}catch (Exception e) {
			log.error( "Exception during file Comparison ", e);
			out.write("JavaCode \n"+e);
			return null;
		}
		
		Gson gson = new Gson();
		return gson.toJson( compareResult );
	}
}
