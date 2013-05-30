<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>Gerrit</title>
    <link rel="stylesheet" href="css/screen.css" media="screen" />
    <link rel="stylesheet" href="css/jquery.treetable.css" />
    <link rel="stylesheet" href="css/jquery.treetable.theme.default.css" />
<link href="css/styles.css" rel="stylesheet" type="text/css">
	<link href="css/dropdown.css" title="default" rel="stylesheet" type="text/css" />
</head>
<body>
	<div class="container">
    	<!-- *************************************** HEADER  -->
        <header class="header">

        <!-- Short Profile Default Section Starts -->
        <div class="profileSection">
        	<nav role="navigation">
                <ul class="main-nav">
                    <li>
                        <a href="#">All <i class="icon-chevron-down"></i></a>
                        <div class="dropdown">
                        	<em class="subMenuTopArrow"></em>
                            <ul class="subMenu">
                                <li><a href="#">Open</a></li>
                                <li><a href="#">Merged</a></li>
                                <li><a href="#">Abandoned</a></li>                                                        
                            </ul>
                        </div>
                     </li>
                     <li><span class="vrLine"> | </span></li>
                    <li>
                        <a href="#">Projects <i class="icon-chevron-down"></i></a>
                        <div class="dropdown">
                        <em class="subMenuTopArrow"></em>
                            <ul class="subMenu">
                                <li><a href="#">List</a></li>
                            </ul>
                        </div>
                     </li>
                     <li><span class="vrLine"> | </span></li>
                    <li>
                        <a href="#">Documentation <i class="icon-chevron-down"></i></a>
                        <div class="dropdown">
                        	<em class="subMenuTopArrow"></em>
                            <ul class="subMenu">
                                <li><a href="#">Index</a></li>
                                <li><a href="#">Searching</a></li>
                                <li><a href="#">Uploading</a></li>                                                        
                                <li><a href="#">Access Controls</a></li>                                                                                    
                            </ul>
                        </div>
                     </li>
                </ul>
            </nav>
            <p role="modified">
            	Uploaded : <span>Apr 1, 2013 2:04 AM</span>      Updated  : <span>Apr 1, 2013 2:04 AM</span> 
            </p>
            <div role="user-account">
           	 <nav role="navigation">
                <ul class="user-nav">
                    <li>
                        <a href="#">Steffen Pingel <i class="icon-chevron-down"></i></a>
                        <div class="dropdown">
                        	<em class="subMenuTopArrow"></em>
                            <ul class="subMenu">
                                <li><a href="#">My Profile</a></li>
                                <li><a href="#">Change Password</a></li>
                            </ul>
                        </div>
                     </li>
                      <li>
                      	<button value="logout" class="button secondary logoutButton">SIGNOUT</button>
                     </li>
                </ul>
            	</nav>
            </div>
        </div>
        <!-- Short Profile Default Section Ends -->
        
        <!-- Full Profile Section Show/Hide Panel Starts -->
        <div class="showContent">
        	
             <!-- Full Profile Split Controller Starts -->
            <div class="splitter vr_spliter vrSplitBar">
                <div class="vrSplitWrapper">
                
                    <em class="tinyDot"></em>
                     <i class="icon-chevron-up"></i>
                     <i class="icon-chevron-down"></i>
                    <em class="tinyDot"></em>    
                                         
                </div>
            </div>
             <!-- Full Profile Split Controller Ends -->            
             
             <!-- Full Profile Details Starts -->
            <div class="content"> 
                <div class="headerContent">
                <div class="ownerInfo">
                	<div role="owner-name">
                    	<img src="images/profilePic.png" />
                    	<span>
                        	<a href="#">Steffen Pingel</a>
                        	OWNER
                       	</span>
                    </div>
                   
                    <div role="owner-info">
                    	<div class="lFloat col col1">
                            <div class="ownerInfoRow">
                                <label>Change-Id</label>
                                <p>Ie9557bc1165e9da56e5c63a1f5c9d51acb1eebad</p>
                            </div>
                            
                            <div class="ownerInfoRow">
                            <label>Project</label>
                            <p><a href="#">mylyn/org.eclipse.mylyn.incubator</a></p>
                            </div>
                            
                            <div class="ownerInfoRow">
                            <label>Branch</label>
                            <p><a href="#">master</a></p>
                            </div>
                            
                            <div class="ownerInfoRow">
                            <label>Topic</label>
                            <p></p>
                            </div>
                        </div>
                        <div class="lFloat col col2">
                        	<div class="ownerInfoRow">
                            <label>Commit Msg</label>
                            <p>383907:.. \minimum JRE to 1.6</p>
                            </div>
                            
                            <div class="ownerInfoRow">
                            <label>Task URL</label>
                            <p><a href="#">bugs.eclipse.org/bugs/show_bug.cgi?id=383907</a></p>
                            </div>
                        </div>
                        
                        
                    </div>
                    </div>
                    
                    <div class="reviewWrapper">
                        <div role="task-status">
                            <img src="images/progressInReview.png" />
                        </div>
                        <div role="reviwers">
                        	<span> Reviewing by :</span>
                            <span role="reviwer-names"><i class="icon-chevron-right"></i> <a href="#">John Doe</a>, <a href="#">John Doe</a>, <a href="#">John Doe</a></span>
                            </ul>
                        </div>
                    </div>
                    
                </div>
            </div>  
            <!-- Full Profile Details Starts -->
                     
        </div>
        <!-- Full Profile Section Show/Hide Panel Ends -->
        
        </header>
        
    	<!-- ****************************************************************** LEFT PANEL (TREE & LIVE STREAMING)   -->        
        <section class="navigation hideComments" >
        	<div id="navController" class="expandTree">
        
                <div class="showContent">
                    
                    <div class="splitter hr_spliter hrSplitBar" name="vrHalfSpliter">
                        <div class="hrSplitWrapper">
                        
                            <em class="tinyDot"></em>
                            	<i class="icon-chevron-left"></i>
                            	<i class="icon-chevron-right"></i>
                            <em class="tinyDot"></em>
                    
                        </div>
                    </div>
                       
                    <div class="content"> 
                        <div class="navigationWrapper">
                                
                                <div class="patches">
                                <div class="fileNotFoundValid">
                                	
                                	<i class="icon-exclamation-sign"></i>Oooops! File not found!
                                </div>
                                	
                                <div class="overlay"></div> 
                                <div class="patchesPanel">
                                	<div class="patchesWrapper">
                                    	<span class="commentCounts commentCountsOn">
                                           	 <a href="#">125</a>
                                            </span>
                                        <div class="dropdown dropdownDefaultValue"> 
                                            <div role="selected"><a href="#"><i class="icon-chevron-down"></i><span class="label" id="checkCurrentPatch">Patch 1</span></a>
                                              </div> 
                                            <div role="options"> 
                                                <ul class="subMenu">
                                            	 	<li><a href="#" data-dd-id='patch1'>Patch 1</a></li>
                                                    <li><a href="#" data-dd-id='patch2'>Patch 2</a></li>
                                                    <li><a href="#" data-dd-id='patch3'>Patch 3</a></li>
                                                    <li><a href="#" data-dd-id='patch4'>Patch 4</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                        
                                    </div>
                                    <div class="search-container">
                                		<input type="text" value="" class="inputText" id="search" placeholder="Start Searching …" name="term" id="term" autocomplete="off">
                                    	<button class="image-button search no-margin" id="searchAction" type="submit">Search</button>
                                	</div>
        						</div>
                                
          						<div class="treeStructure">
                                <table id="patch1" cellpadding="0" cellspacing="0">
       
        <tbody>
      <tr data-tt-id='1'><td><span class='folder'>Patch 1</span></td></tr>
      <tr data-tt-id='1-1' data-tt-parent-id='1'><td ><span class='folder'>Images</span></td></tr>
      <tr data-tt-id='1-1-1' data-tt-parent-id='1-1'><td ><span class='folder'>BigImages</span></td></tr>
          <tr data-tt-id='1-1-1-1' data-tt-parent-id='1-1-1'><td class="modified comment"><span class='file'>PNG File.png</span></td></tr>
          <tr data-tt-id='1-1-1-2' data-tt-parent-id='1-1-1'><td class="deleted comment"><span class='file'>PNG File.png</span></td></tr>
          <tr data-tt-id='1-1-1-3' data-tt-parent-id='1-1-1'><td class="new comment"><span class='file'>GIF File.gif</span></td></tr>

      <tr data-tt-id='1-2' data-tt-parent-id='1'><td><span class='folder'>PDF</span></td></tr>
          <tr data-tt-id='1-2-1' data-tt-parent-id='1-2'><td><span class='file'>BlueWhale.pdf</span></td></tr>
          <tr data-tt-id='1-2-2' data-tt-parent-id='1-2'  ><td><span class='file'>Shark.pdf</span></td></tr>      
      <tr data-tt-id='1-3' data-tt-parent-id='1'><td><span class='folder'>Code/Text/Styles</span></td></tr>
          <tr data-tt-id='1-3-1' data-tt-parent-id='1-3'><td><span class='file'>PHTML.html</span></td></tr>
          <tr data-tt-id='1-3-2' data-tt-parent-id='1-3'><td><span class='file'>PHP.js</span></td></tr>
          <tr data-tt-id='1-3-3' data-tt-parent-id='1-3'><td><span class='file'>JS.js</span></td></tr>
          <tr data-tt-id='1-3-4' data-tt-parent-id='1-3'><td><span class='file'>CSS.css</span></td></tr>
          <tr data-tt-id='1-3-5' data-tt-parent-id='1-3'  ><td><span class='file'>ASP.asp</span></td></tr>
          <tr data-tt-id='1-3-6' data-tt-parent-id='1-3'><td><span class='file'>Java.jsp</span></td></tr>
          <tr data-tt-id='1-3-7' data-tt-parent-id='1-3' ><td class="modified comment"><span class='file'>Properties</span></td></tr>
          <tr data-tt-id='1-3-8' data-tt-parent-id='1-3'><td><span class='file'>Text.txt</span></td></tr>
      <tr data-tt-id='1-4' data-tt-parent-id='1'><td><span class='folder'>Zip/Jar/Rar/Compresed</span></td></tr>
          <tr data-tt-id='1-4-1' data-tt-parent-id='1-4'><td><span class='file'>fullProject.zip</span></td></tr>
          <tr data-tt-id='1-4-2' data-tt-parent-id='1-4'  ><td><span class='file'>Module.rar</span></td></tr> 
          <tr data-tt-id='1-4-3' data-tt-parent-id='1-4'><td><span class='file'>Build.jar</span></td></tr>
          <tr data-tt-id='1-4-4' data-tt-parent-id='1-4'><td><span class='file'>Private.dmg</span></td></tr> 
      
      
        </tbody>
      </table>
      
      
      <table id="patch2" cellpadding="0" cellspacing="0">
       
        <tbody>
      <tr data-tt-id='1'><td><span class='folder'>Patch 2</span></td></tr>
      <tr data-tt-id='1-1' data-tt-parent-id='1'><td ><span class='folder'>Images</span></td></tr>
      <tr data-tt-id='1-1-1' data-tt-parent-id='1-1'><td ><span class='folder'>BigImages</span></td></tr>
          <tr data-tt-id='1-1-1-1' data-tt-parent-id='1-1-1'><td class="modified comment"><span class='file'>JPG File.jpg</span></td></tr>
          <tr data-tt-id='1-1-1-2' data-tt-parent-id='1-1-1'><td class="deleted comment"><span class='file'>PNG File.png</span></td></tr>
          <tr data-tt-id='1-1-1-3' data-tt-parent-id='1-1-1'><td class="new comment"><span class='file'>GIF File.gif</span></td></tr>

      <tr data-tt-id='1-2' data-tt-parent-id='1'><td><span class='folder'>PDF</span></td></tr>
          <tr data-tt-id='1-2-1' data-tt-parent-id='1-2'><td><span class='file'>BlueWhale.pdf</span></td></tr>
          <tr data-tt-id='1-2-2' data-tt-parent-id='1-2'  ><td><span class='file'>Shark.pdf</span></td></tr>      
      <tr data-tt-id='1-3' data-tt-parent-id='1'><td><span class='folder'>Code/Text/Styles</span></td></tr>
          <tr data-tt-id='1-3-1' data-tt-parent-id='1-3'><td><span class='file'>PHTML.html</span></td></tr>
          <tr data-tt-id='1-3-2' data-tt-parent-id='1-3'><td><span class='file'>PHP.js</span></td></tr>
          <tr data-tt-id='1-3-3' data-tt-parent-id='1-3'><td><span class='file'>JS.js</span></td></tr>
          <tr data-tt-id='1-3-4' data-tt-parent-id='1-3'><td><span class='file'>CSS.css</span></td></tr>
      
      
        </tbody>
      </table>
      </div>
      </div>
                                
                                <div class="liveStreaming">
                                    <div class="showContent">
                                    
                                         <div class="splitter vr_spliter vrSplitBar" id="splitHalf">
                                             <div class="vrSplitWrapper">
                                             
                                                <em class="tinyDot"></em>
                                                 <i class="icon-chevron-up"></i>
                                                 <i class="icon-chevron-down"></i>
                                                <em class="tinyDot"></em>
                                                    
                                             </div>
                                         </div>  
                                                
                                        <div class="content"> 
                                            <div class="liveStreamList">
                            
                                            
                                            </div>
                                       </div> 
                                           
                                    </div>
                                </div>
                        
                        </div>
                    </div>            
                </div>
                <div class="copyright">Copyright &copy; 2013 imaginea</div>
            </div>
        
         
        </section>
        
        <section class="main">
            <div class="mainContianer">
                <div class="zoomAction" >  
                	<ul class="showFullScreen">
                    	<li class="controlScreen fullScreen"><i class="icon-fullscreen"></i></li>
                        <li class="controlScreen resizeScreen"><i class="icon-resize-small"></i></li>
                     </ul>
      
            	</div>
                <div class="playGround">
                
    Gold futures in India, the world's biggest buyer of the metal, dropped nearly 3 percent to hit their lowest level in 15-1/2 months, mirroring losses in the world market.

    (Photo: Reuters Pictures)
    
    Gold futures in India, the world's biggest buyer of the metal, dropped nearly 3 percent to hit their lowest level in 15-1/2 months, mirroring losses in the world market.

    (Photo: Reuters Pictures)
    
    Gold futures in India, the world's biggest buyer of the metal, dropped nearly 3 percent to hit their lowest level in 15-1/2 months, mirroring losses in the world market.

    (Photo: Reuters Pictures)
    
    Gold futures in India, the world's biggest buyer of the metal, dropped nearly 3 percent to hit their lowest level in 15-1/2 months, mirroring losses in the world market.

    (Photo: Reuters Pictures) <%= 500*10 %>
                </div>
            </div>
           
        </section>
    
	</div>
    

<script src="js/jquery.js"></script>
    <script src="js/jquery-ui.js"></script>
    <script src="js/jquery.treetable.js"></script>
    	<script type="text/javascript" src="js/dropdown.js"></script>
		<script src="js/script.js"></script>
</body>
</html>
