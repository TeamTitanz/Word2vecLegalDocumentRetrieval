/*******************************************************************************
 * Copyright 2016 Titans
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 ******************************************************************************/
package org.titans.fyp.webcrawler.models;


import org.titans.fyp.webcrawler.utils.Common;
import org.titans.fyp.webcrawler.utils.Hasher;

import java.sql.Timestamp;

public class Domain {
    private String domainHash;
    private String domainUrl;
    private boolean activated;
    private Timestamp modified;
    private Timestamp created;

    public Domain() {
    }

    public Domain(String domainUrl) throws Exception {
        //Hashing is useful when crawling billions of webpages.
        this.domainHash = Hasher.toSha256(domainUrl);
        this.domainUrl = domainUrl;
        this.activated = true;
        this.modified = Common.getTimeStamp();
        this.created = Common.getTimeStamp(); //To store crawled time stamps
    }

    public Domain(String domainHash, String domainUrl, boolean activated, Timestamp modified, Timestamp created) {
        this.domainHash = domainHash;
        this.domainUrl = domainUrl;
        this.activated = activated;
        this.modified = modified;
        this.created = created;
    }

    public String getDomainHash() {
        return domainHash;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public Timestamp getModified() {
        return modified;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setDomainHash(String domainHash) {
        this.domainHash = domainHash;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }


}
