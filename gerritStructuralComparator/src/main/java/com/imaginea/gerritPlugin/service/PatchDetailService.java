package com.imaginea.gerritPlugin.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gerrit.common.errors.NoSuchEntityException;
import com.google.gerrit.extensions.annotations.Export;
import com.google.gerrit.httpd.rpc.BaseServiceImplementation.Failure;
import com.google.gerrit.reviewdb.client.Account;
import com.google.gerrit.reviewdb.client.Change;
import com.google.gerrit.reviewdb.client.Patch;
import com.google.gerrit.reviewdb.client.PatchLineComment;
import com.google.gerrit.reviewdb.client.PatchSet;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.ChangeUtil;
import com.google.gerrit.server.CurrentUser;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.project.ChangeControl;
import com.google.gerrit.server.project.NoSuchChangeException;
import com.google.gwtorm.server.OrmException;
import com.google.gwtorm.server.ResultSet;
import com.google.gwtorm.server.SchemaFactory;
import com.google.inject.Inject;

@Export("/patchDetailService")
public class PatchDetailService extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static org.apache.log4j.Logger log = Logger.getLogger(PatchDetailService.class);
	
	private final ChangeControl.Factory changeControlFactory;
	private ReviewDb db = null;
	private final SchemaFactory<ReviewDb> dbFactory;
	private ChangeControl control;

	@Inject
	public PatchDetailService(SchemaFactory<ReviewDb> dbFactory,
			final ChangeControl.Factory changeControlFactory){
		this.dbFactory = dbFactory;
		this.changeControlFactory = changeControlFactory;
	}
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String reviewerComment = req.getParameter("message");
		log.debug("reviewerComment "+reviewerComment);
		int line = Integer.valueOf(req.getParameter("lineNumber"));
		log.debug("lineNumber "+line);
		String change_id = "0";
		String patchId = "1";
		int tmpSide = Integer.valueOf(req.getParameter("side"));
		log.debug("Side "+tmpSide);
		final short side;
		if( tmpSide == 1 ){
			side = (short) 0;
		}else if( tmpSide == 3 ){
			side = (short) 1;
		}else{
			side = (short) 1;
			log.debug("Invalid side");
		}
		
		String changeDetail = req.getParameter("changeDetail");
		log.debug("changeDetail "+changeDetail);
		String[] tmpArray = changeDetail.split(",");
		if( tmpArray.length == 3 ){
			patchId = tmpArray[1];
			String[] tmpChangeId = tmpArray[0].split("/");
			if( tmpChangeId.length == 5 ){
				change_id = tmpChangeId[4];
			}
		}
		String tmpFileName = tmpArray[2];
		String fileName = tmpFileName.substring(0, tmpFileName.length()-2 );
		
		Change.Id change = new Change.Id(Integer.valueOf(change_id));
		final Patch.Key parentKey;
		try {
			control = changeControlFactory.validateFor(change);
		} catch (NoSuchChangeException e1) {
			log.debug("NoSuchChangeException ", e1);
		} catch (OrmException e1) {
			log.debug("OrmException ", e1);
		}
		
		final CurrentUser user = control.getCurrentUser();
		final Account.Id me = user instanceof IdentifiedUser ? ((IdentifiedUser) user).getAccountId(): null;
		
		PatchSet.Id idSideB = new PatchSet.Id(change, Integer.valueOf(patchId));
       
		
		parentKey = new Patch.Key(idSideB, fileName);
			
		
        
		 log.debug("account.getId() " + me);
		 
		 try {
			 log.debug("Calling saveDraftFactory");
			 Set<PatchLineComment> draftMessage = loadDraftMessage( change, idSideB );
			 PatchLineComment comment = null;
			 log.debug("idSideB::"+idSideB);
			 log.debug("DraftMessage Size:: "+draftMessage.size());
			 
			 Iterator itr = draftMessage.iterator();
			 String flag =  req.getParameter("flag");
			 log.debug("flag "+flag);
			 if( draftMessage.size() > 0 ){
				 while( itr.hasNext() ){
					 PatchLineComment lineComment = (PatchLineComment)itr.next();
					 if( lineComment.getLine() == line && lineComment.getSide() == side ){
						 log.debug("UUID::"+lineComment.getKey().get());
						 comment = new PatchLineComment(new PatchLineComment.Key(parentKey, lineComment.getKey().get()),line, me, null);
						 if( flag.equalsIgnoreCase("discard")){
							 try {
								deleteDraft(parentKey, lineComment.getKey().get());
							} catch (Failure e) {
								log.debug("Failure ",e);
							}
						 }
					 }
				 }
				 if( null == comment ){
					 comment = new PatchLineComment(new PatchLineComment.Key(parentKey, null),line, me, null);
				 }
			 }else{
				 comment = new PatchLineComment(new PatchLineComment.Key(parentKey, null),line, me, null);
			 }
			 comment.setSide(side);
			 comment.setMessage( reviewerComment );
			 
			 if( flag.equalsIgnoreCase("save")){
				 saveDraft(comment);
			 }
		} catch (OrmException e) {
			log.debug("OrmException ", e);
		} catch (NoSuchChangeException e) {
			log.debug("NoSuchChangeException ", e);
		} 
	}
	
	
	// Arguement require: patchLineComment Object
	 private PatchLineComment saveDraft( PatchLineComment comment ) throws NoSuchChangeException, OrmException {
		    if (comment.getStatus() != PatchLineComment.Status.DRAFT) {
		      throw new IllegalStateException("Comment published");
		    }

		    final Patch.Key patchKey = comment.getKey().getParentKey();
		    final PatchSet.Id patchSetId = patchKey.getParentKey();
		    final Change.Id changeId = patchKey.getParentKey().getParentKey();

		    try {
				db = dbFactory.open();
			} catch (OrmException e) {
				log.error("OrmException::",e);
			}
		    
		    db.changes().beginTransaction(changeId);
		    try {
		      changeControlFactory.validateFor(changeId);
		      if (db.patchSets().get(patchSetId) == null) {
		        throw new NoSuchChangeException(changeId);
		      }

		      CurrentUser user = control.getCurrentUser();
			  Account.Id me = user instanceof IdentifiedUser ? ((IdentifiedUser) user).getAccountId(): null;
		      
			  if (comment.getKey().get() == null) {
		        if (comment.getLine() < 1) {
		          throw new IllegalStateException("Comment line must be >= 1, not "
		              + comment.getLine());
		        }

		        if (comment.getParentUuid() != null) {
		          final PatchLineComment parent =
		              db.patchComments().get(
		                  new PatchLineComment.Key(patchKey, comment.getParentUuid()));
		          if (parent == null || parent.getSide() != comment.getSide()) {
		            throw new IllegalStateException("Parent comment must be on same side");
		          }
		        }

			        
		        final PatchLineComment nc =
		            new PatchLineComment(new PatchLineComment.Key(patchKey, ChangeUtil
		                .messageUUID(db)), comment.getLine(), me, comment.getParentUuid());
		        log.debug("patchKey "+patchKey);
		        log.debug("ChangeUtil.messageUUID(db) "+ChangeUtil.messageUUID(db));
		        log.debug("me "+me);
		        log.debug("comment.getParentUuid() "+comment.getParentUuid());
		        nc.setSide(comment.getSide());
		        nc.setMessage(comment.getMessage());
		        log.debug("Collections.singleton(nc)"+Collections.singleton(nc));
		        db.patchComments().insert(Collections.singleton(nc));
		        db.commit();
		        return nc;

		      } else {
		        if (!me.equals(comment.getAuthor())) {
		          throw new NoSuchChangeException(changeId);
		        }
		        comment.updated();
		        db.patchComments().update(Collections.singleton(comment));
		        db.commit();
		        db.close();
		        return comment;
		      }
		    } finally {
		      db.rollback();
		      db.close();
		    }
		  }
	 
	 // Arguement require: changeId and UUID
	private void deleteDraft(Patch.Key patch_key, String UUID) throws OrmException, Failure{
		 log.debug("UUID from deleteDraft() method "+UUID);
		 
		 PatchLineComment.Key commentKey = new PatchLineComment.Key(patch_key, UUID);
		 Change.Id changeId = commentKey.getParentKey().getParentKey().getParentKey();
		 log.debug("commentKey:: "+commentKey);
	     log.debug("Change.Id:: "+changeId);
	        try {
				db = dbFactory.open();
			} catch (OrmException e) {
				log.error("OrmException::",e);
			}
	        
	        db.changes().beginTransaction(changeId);
	        
	        try {
	          log.debug("Inside try block PatchLineComment ");
	          PatchLineComment comment = db.patchComments().get( commentKey );
	          log.debug("PatchLineComment:: "+comment);
	          if (comment == null) {
	            throw new Failure(new NoSuchEntityException());
	          }
	          
	          final CurrentUser user = control.getCurrentUser();
	          final Account.Id me = user instanceof IdentifiedUser ? ((IdentifiedUser) user).getAccountId(): null;
	      	  log.debug(" comment.getAuthor()::"+comment.getAuthor() );
	          if (!me.equals(comment.getAuthor())) {
	            throw new Failure(new NoSuchEntityException());
	          }
	          if (comment.getStatus() != PatchLineComment.Status.DRAFT) {
	            throw new Failure(new IllegalStateException("Comment published"));
	          }
	          db.patchComments().delete(Collections.singleton(comment));
	          db.commit();
	          db.close();
	        } finally {
	          db.rollback();
	          db.close();
	        }
	 }
	 
	// Arguement require: changeId and pastchSetId
	private Set<PatchLineComment> loadDraftMessage( Change.Id changeId, PatchSet.Id patchSetId ) throws OrmException {
		log.debug("loadPatchSets() Method Arguement "+changeId); 
		try {
			db = dbFactory.open();
		} catch (OrmException e) {
			log.error("OrmException::",e);
		}
	    ResultSet<PatchSet> source = db.patchSets().byChange(changeId);
	    List<PatchSet> patches = new ArrayList<PatchSet>();
	    Set<PatchLineComment> patchesDraftSet = new HashSet<PatchLineComment>();
	   // HashMap< PatchSet.Id , PatchLineComment> patchDraftMap = new HashMap<PatchSet.Id, PatchLineComment>();
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
	  		        		if( me.equals(comment.getAuthor())){
	  		        			patchesDraftSet.add(comment);
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
