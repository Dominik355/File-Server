package org.dbilik.fileServer.service;

import javax.servlet.http.HttpServletResponse;

public interface DownloadService {

    public void download(String id, HttpServletResponse response);

}
