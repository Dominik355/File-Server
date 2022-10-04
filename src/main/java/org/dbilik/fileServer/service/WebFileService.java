package org.dbilik.fileServer.service;

/**
 * Service called by controller. Handles request, authorization etc.
 * Calls IOFileService which handles pure IO stuff without knowing about some web layer in this application.
 * So purpose is to totally separate these parts and for IOFileService makes every method independent on caller.
 */
public interface WebFileService {
}
