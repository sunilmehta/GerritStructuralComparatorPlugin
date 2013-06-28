package com.imaginea.gerritPlugin.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gerrit.reviewdb.client.Patch;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.patch.PatchList;
import com.google.gerrit.server.patch.PatchListCache;
import com.google.gerrit.server.patch.PatchListNotAvailableException;
import com.google.gwtorm.server.OrmException;
import com.google.gwtorm.server.ResultSet;
import com.google.gwtorm.server.SchemaFactory;
import com.imaginea.gerritPlugin.model.Change;
import com.imaginea.gerritPlugin.model.ChangeID;
import com.imaginea.gerritPlugin.model.PatchSet;

public class GerritChangeProcessingService {

	private static org.apache.log4j.Logger log = Logger.getLogger(GerritChangeProcessingService.class);
	
	private String requestHostName = null;
	
	// Method Added by: Amit Kumar
	public List<ChangeID> prepareChangeIdList(final SchemaFactory<ReviewDb> dbFactory,
			final PatchListCache patchListCache, String reqHostName) {
		ReviewDb reviewDb = null;
		try {
			reviewDb = dbFactory.open();
		} catch (OrmException e) {
			log.error("OrmException::",e);
		}
		List<ChangeID> changeIdList = new ArrayList<ChangeID>();
		ChangeID idList = null;
		try {
			if( null != reviewDb ){
				for (com.google.gerrit.reviewdb.client.Change change : reviewDb.changes().all().toList()) {
					idList = new ChangeID();
					idList.setChange_id(change.getKey().get());
					idList.setCommitMsg(change.getSubject());
					changeIdList.add( idList );
			}
			}
			
		} catch (OrmException e) {
			log.error("OrmException::",e);
		}
		reviewDb.close();
		log.debug("ChangeId List Size::"+changeIdList.size());
		return changeIdList;
		
	}
	
	
	public Change fetchChangesbyChangeId(final SchemaFactory<ReviewDb> dbFactory,
			final PatchListCache patchListCache, String reqHostName, String changeId)
			 {
		ReviewDb reviewDb = null ;
		try {
			reviewDb = dbFactory.open();
		} catch (OrmException e) {
			log.error("OrmException::",e);
		}
		requestHostName = reqHostName;
		Change custChange = null;
		try {
			if(null != reviewDb){
				for (com.google.gerrit.reviewdb.client.Change change : reviewDb
						.changes().all().toList()) {
					if( change.getKey().get().equals(changeId)){
						custChange = buildCustomChange(change);
						custChange.setOwner(reviewDb.accounts().get(change.getOwner())
								.getFullName());
						
						ResultSet<com.google.gerrit.reviewdb.client.PatchSet> rs = reviewDb
								.patchSets().byChange(change.getId());
						List<com.google.gerrit.reviewdb.client.PatchSet> patchSetsByChangeID = rs
								.toList();
						
						List<PatchSet> customPatchSets = buildPatchSets(
								patchSetsByChangeID, patchListCache, change);
						
						custChange.setPatchSets(customPatchSets);
					}
				}
			}
			
		} catch (OrmException e) {
			log.error("OrmException::",e);
		} catch (PatchListNotAvailableException e) {
			log.error("PatchListNotAvailableException::",e);
		}
		if(null != reviewDb){
			reviewDb.close();
		}
		return custChange;
	}
	

	private List<PatchSet> buildPatchSets(
			List<com.google.gerrit.reviewdb.client.PatchSet> patchSetsByChangeID,
			PatchListCache patchListCache,
			com.google.gerrit.reviewdb.client.Change change)
			throws PatchListNotAvailableException {

		List<PatchSet> customPatchSets = new ArrayList<PatchSet>();

		for (com.google.gerrit.reviewdb.client.PatchSet patchSet : patchSetsByChangeID) {
			PatchSet customPatchSet = new PatchSet();
			customPatchSet.setPatchSetId(patchSet.getPatchSetId());
			customPatchSet.setChangeId(change.getChangeId());
			PatchList list = patchListCache.get(change, patchSet);

			final List<Patch> patches = list.toPatchList(patchSet.getId());
			
			customPatchSet.setPatchList(buildPatcheList(patches));
			
			customPatchSets.add(customPatchSet);
		}

		return customPatchSets;
	}


	private List<com.imaginea.gerritPlugin.model.Patch> buildPatcheList(List<Patch> patches){
		List<com.imaginea.gerritPlugin.model.Patch> patchList = new ArrayList<com.imaginea.gerritPlugin.model.Patch>();
		 
		for (Patch patch : patches) {
			if ("/COMMIT_MSG".equals(patch.getFileName()))
				continue;
			
			String fileURL = "http://" + requestHostName + "/cat/"
					+patch.getKey().getParentKey().toString() + ","
					+ patch.getFileName() + "^0";
			
			com.imaginea.gerritPlugin.model.Patch customPatch = new com.imaginea.gerritPlugin.model.Patch( patch, fileURL );
			patchList.add(customPatch);
		}
		return patchList;
	}

	private Change buildCustomChange(com.google.gerrit.reviewdb.client.Change change) {
		Change custChange = new Change( change );
		return custChange;
	}
	
}