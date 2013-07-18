package com.imaginea.gerritPlugin.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gerrit.reviewdb.client.Account;
import com.google.gerrit.reviewdb.client.Change;
import com.google.gerrit.reviewdb.client.PatchLineComment;
import com.google.gerrit.reviewdb.client.PatchSet;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.CurrentUser;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.project.ChangeControl;
import com.google.gerrit.server.project.NoSuchChangeException;
import com.google.gwtorm.server.OrmException;
import com.google.gwtorm.server.ResultSet;
import com.google.gwtorm.server.SchemaFactory;
import com.imaginea.javaStructuralComparator.domain.DraftMessage;

public class DraftUtil {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(DraftUtil.class);
	private static ReviewDb db = null;
	private static ChangeControl control;
	
	public static List<DraftMessage> loadDraftMessage( SchemaFactory<ReviewDb> dbFactory, ChangeControl.Factory changeControlFactory, String url ) throws OrmException {
		int id = 0;
		int change_id = 0;
		String[] changeDetails =url.split(",");
		if( changeDetails.length == 3 ){
			id = Integer.valueOf( changeDetails[1] );
			String tmpUrl = changeDetails[0];
			String[] tmpChangeId = tmpUrl.split("/");
			if( tmpChangeId.length == 5 ){
				change_id = Integer.valueOf(tmpChangeId[4]);
			}else{
				return null;
			}
		}
		
		Change.Id changeId = new Change.Id( change_id );
		PatchSet.Id patchSetId = new PatchSet.Id(changeId, id);
		
		log.debug("loadPatchSets() Method Arguement "+changeId); 
		try {
			db = dbFactory.open();
		} catch (OrmException e) {
			log.error("OrmException::",e);
		}
		
		try {
			control = changeControlFactory.validateFor(changeId);
		} catch (NoSuchChangeException e) {
			log.debug("NoSuchChangeException ",e);
			e.printStackTrace();
		}
		
	    ResultSet<PatchSet> source = db.patchSets().byChange(changeId);
	    List<PatchSet> patches = new ArrayList<PatchSet>();
	    List<DraftMessage> patchesDraftSet = new ArrayList<DraftMessage>();
	    final CurrentUser user = control.getCurrentUser();
	    final Account.Id me = user instanceof IdentifiedUser ? ((IdentifiedUser) user).getAccountId():null;
	    for (PatchSet ps : source) {
	      final PatchSet.Id psId = ps.getId();
	      if( psId.equals( patchSetId) ){
	    	  if (control.isPatchVisible(ps, db)) {
	  	        patches.add(ps);
	  	        if (me != null && db.patchComments().draftByPatchSetAuthor(psId, me).iterator().hasNext()) {
	  	        	log.debug("patchesWithDraftComments "+psId);
	  	        	ResultSet<PatchLineComment> patchComment = db.patchComments().byPatchSet(psId);
	  	        	Iterator itr = patchComment.iterator();
	  	        	PatchLineComment comment = null;
	  	        	while( itr.hasNext() ){
	  	        			comment = (PatchLineComment)itr.next();
	  		        		log.debug("Message "+comment.getMessage());
	  		        		log.debug("Side "+comment.getSide());
	  		        		log.debug("Line Number "+comment.getLine());
	  		        		log.debug("Author "+comment.getAuthor());
	  		        		log.debug("Written On::"+comment.getWrittenOn());
	  		        		if( me.equals(comment.getAuthor())){
	  		        			DraftMessage message = new DraftMessage();
	  		        			message.setLine(comment.getLine());
	  		        			message.setMessage(comment.getMessage());
	  		        			message.setSide(comment.getSide());
	  		        			message.setWrittenOn(comment.getWrittenOn());
	  		        			patchesDraftSet.add(message);
	  		        		}
	  		        		//patchDraftMap.put(psId , comment);
	  	        	}
	  	        }
	  	      }
	      }
	    }
	    db.close();
	    return patchesDraftSet;
	  }
}
