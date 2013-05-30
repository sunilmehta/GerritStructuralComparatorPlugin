package com.imaginea.gerritPlugin.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gerrit.extensions.annotations.Export;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.patch.PatchListCache;
import com.google.gson.Gson;
import com.google.gwtorm.server.SchemaFactory;
import com.google.inject.Inject;
import com.imaginea.gerritPlugin.model.ChangeDetails;
import com.imaginea.gerritPlugin.model.ChangeID;
import com.imaginea.gerritPlugin.utils.FileDataRetrivalService;
import com.imaginea.javaStructuralComparator.domain.ComparisonResult;
import com.imaginea.javaStructuralComparator.repo.ComparatorImpl;

@Export("/gerritPlugin")
public class GerritPlugin extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final PatchListCache patchListCache;
	private final SchemaFactory<ReviewDb> dbFactory;

	@Inject
	public GerritPlugin(SchemaFactory<ReviewDb> dbFactory,
			final PatchListCache patchListCache) {
		this.dbFactory = dbFactory;
		this.patchListCache = patchListCache;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		try {
			String PatchSetUrl = req.getParameter("url");
			String PatchSetUrl1 = req.getParameter("patchSetURL1");
			String PatchSetUrl2 = req.getParameter("patchSetURL2");
			System.out.println("fileURL1 "+PatchSetUrl);
			String BaseUrl = "";
			String fileData1 ="";
			String fileData2 ="";
			if ( null != PatchSetUrl )
				BaseUrl = PatchSetUrl.substring(0, PatchSetUrl.length()-1).concat("1");

			if( null != PatchSetUrl && null != BaseUrl){
				try{
					 fileData2 = FileDataRetrivalService.getFileDataStream(PatchSetUrl);
					 fileData1 = FileDataRetrivalService.getFileDataStream(BaseUrl);
					 ComparatorImpl comparatorImpl = new ComparatorImpl();
					 ComparisonResult result = comparatorImpl.compare(fileData1 ,fileData2);
					 Gson gson = new Gson();
					 out.write(gson.toJson( result ));
					 System.out.println( "Structural Comparator Result \n:: "+gson.toJson( result ) );
				}catch (Exception e) {
					System.out.println("Exception caught");
					fileData1 = "";
					out.write("JavaCode \n"+fileData2);
				}
			}else if( null != PatchSetUrl1 && null != PatchSetUrl2 ){
				fileData1 = FileDataRetrivalService.getFileDataStream(PatchSetUrl1);
				fileData2 = FileDataRetrivalService.getFileDataStream(PatchSetUrl2);
				ComparatorImpl comparatorImpl = new ComparatorImpl();
				ComparisonResult result = comparatorImpl.compare(fileData1 ,fileData2);
			 	Gson gson = new Gson();
			    out.write(gson.toJson( result ));
			}else{
				String reqHostName = req.getServerName() + ":" + req.getServerPort();
				ChangeDetails changeDetail = new ChangeDetails();
				GerritChangeProcessingService serviceObj = new GerritChangeProcessingService();
				// List<Change> changeList = serviceObj.fetchChanges(dbFactory, patchListCache, reqHostName);
				List<ChangeID> changeIdList = serviceObj.prepareChangeIdList(dbFactory, patchListCache, reqHostName);
				changeDetail.setChangeIDs(changeIdList);
				String reqChangeId = req.getParameter("id");
				System.out.println("Request Parameter "+reqChangeId);
				if( null != reqChangeId ){
					changeDetail.setChangeIDs(null);
					changeDetail.setChange( serviceObj.fetchChangesbyChangeId(dbFactory, patchListCache, reqHostName, reqChangeId) );
				}else if( changeIdList.size() > 0){
					changeDetail.setChangeIDs(changeIdList);
					changeDetail.setChange( serviceObj.fetchChangesbyChangeId(dbFactory, patchListCache, reqHostName, changeIdList.get(0).getChange_id()) );
				}
				
				Gson gson = new Gson();
				// String json = gson.toJson(changeList);
				String changeDetailJSON = gson.toJson(changeDetail);
				//out.write(json);
				System.out.println("changeDetailJSON "+changeDetailJSON);
				out.write(changeDetailJSON);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			out.write("<html><body>Exceptions being thrown</body></html>");
		} finally {
			out.close();
		}
	}
	
	public static void main(String[] args) throws MalformedURLException {
		String PatchSetUrl = "http://localhost:8080/cat/18,2,com/imaginea/HttpModule.java^0";
		String BaseUrl = PatchSetUrl.substring(0, PatchSetUrl.length()-1).concat("1");
		String fileData1 = FileDataRetrivalService.getFileDataStream(BaseUrl);
		String fileData2 = FileDataRetrivalService.getFileDataStream(PatchSetUrl);
		System.out.println("fileData1::"+fileData1);
		System.out.println("fileData2::"+fileData2);
		ComparatorImpl comparatorImpl = new ComparatorImpl();
		ComparisonResult result = comparatorImpl.compare(fileData1 ,fileData2);
		Gson gson = new Gson();
		System.out.println( "Structural Comparator Result:: "+gson.toJson( result ) );
	}
}
