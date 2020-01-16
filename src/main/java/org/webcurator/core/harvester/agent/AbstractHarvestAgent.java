/*
 *  Copyright 2006 The National Library of New Zealand
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.webcurator.core.harvester.agent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The AbstractHarvestAgent implements the methods from HarvestAgent that can be 
 * implemented genericly for all Harvest Agent implementations. 
 * @author nwaight
 */
public abstract class AbstractHarvestAgent implements HarvestAgent {
    /** Map of harvesters. */
    protected ConcurrentHashMap<String, Harvester> harvesters = null;
    /** Memory Warning flag */
    protected boolean memoryWarning = false;
    
    /** Default Constructor. */
    public AbstractHarvestAgent() {
        super();
        harvesters = new ConcurrentHashMap<>();
    }

    /**
     * @see org.webcurator.core.harvester.agent.HarvestAgent#initiateHarvest(java.lang.String, java.util.Map)
     */
    public void initiateHarvest(String aJob, Map<String, String> params) {
        Harvester harvester = new HarvesterH3(aJob);
        harvesters.put(aJob, harvester);
    }

    /**
     * @see org.webcurator.core.harvester.agent.HarvestAgent#recoverHarvests(java.util.List)
     */
    public void recoverHarvests(List<String> activeJobs){}

    /**
     * @see org.webcurator.core.harvester.agent.HarvestAgent#restrictBandwidth(java.lang.String, int)
     */
    public void restrictBandwidth(String aJob, int aBandwidthLimit) {
        Harvester harvester = getHarvester(aJob);
        harvester.restrictBandwidth(aBandwidthLimit);
    }

    /**
     * @see org.webcurator.core.harvester.agent.HarvestAgent#pause(java.lang.String)
     */
    public void pause(String aJob) {
        Harvester harvester = getHarvester(aJob);
        harvester.pause();
    }

    /**
     * @see org.webcurator.core.harvester.agent.HarvestAgent#resume(java.lang.String)
     */
    public void resume(String aJob) {
        Harvester harvester = getHarvester(aJob);
        harvester.resume();
    }

    /**
     * @see org.webcurator.core.harvester.agent.HarvestAgent#abort(java.lang.String)
     */
    public abstract void abort(String aJob);
    
    /**
     * @see org.webcurator.core.harvester.agent.HarvestAgent#stop(java.lang.String)
     */
    public abstract void stop(String aJob);

    /**
     *
     * @see HarvestAgent#isValidProfile(String)
     */
    public abstract boolean isValidProfile(String profile);
    
    /**
     * @see org.webcurator.core.harvester.agent.HarvestAgent#pauseAll()
     */
    public void pauseAll() {
        Harvester h = null;
        Iterator it = harvesters.values().iterator();
        while (it.hasNext()) {
            h = (Harvester) it.next();
            h.pause();
        }
    }

    /**
     * @see org.webcurator.core.harvester.agent.HarvestAgent#resumeAll()
     */
    public void resumeAll() {
        Harvester h = null;
        Iterator it = harvesters.values().iterator();
        while (it.hasNext()) {
            h = (Harvester) it.next();
            h.resume();
        }
    }
    
    /** 
     * Return the harvester allocated to the specified job
     * @param aJob the harvest job name
     * @return the harvester
     */
    protected Harvester getHarvester(String aJob) {                       
        Harvester harvester = (Harvester) harvesters.get(aJob);        
        
        return harvester;
    }    
    
    /**
     * Remove harvester for the complete job
     * @param aJob remove the harvester for the completed job
     */
    protected void removeHarvester(String aJob) {        
        harvesters.remove(aJob);        
    }
    
	/**
	 * @return the memoryWarning flag
	 */
	public boolean getMemoryWarning() {
		return memoryWarning;
	}

	/**
	 * @param memoryWarning the memoryWarning flag to set
	 */
	public void setMemoryWarning(boolean memoryWarning) {
		this.memoryWarning = memoryWarning;
	}
}