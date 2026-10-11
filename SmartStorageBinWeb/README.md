# Smart Storage Bin - Web Application

An interactive, responsive full-stack web simulation of the **Smart Storage Bin** mobile file-lifecycle management system.

---

## 🌟 Key Features

1. **File Ingestion & Classification**:
   - Upload any real file from your PC (drag & drop or native picker).
   - Create 0-byte empty notes/files (`.txt`, `.docx`, `.pdf`, `.json`, `.py`, `.html`, `.md`).
   - Dynamic retention durations: 1, 3, 7, 15, 30, 60 days, or Permanent.

2. **File Viewer & Editor**:
   - Click any file to open interactive viewers.
   - Live in-browser text and code editor with live editing, size recalculation, and file saving.
   - Stylized PDF document reader with formatted outline.
   - High-definition image preview & media player simulation.
   - Direct download of files to your device.

3. **Lifecycle Automation & Zero Auto-Deletion**:
   - Dynamic expiry warnings for expired temporary files.
   - Extend retention period (+7 Days) or convert to permanent with 1 click.
   - Two-phase Recycle Bin with full restoration support.

4. **Storage & Deduplication Engine**:
   - SHA-256 byte-for-byte duplicate clustering.
   - Visual storage capacity meters and category distribution charts.

---

## 🚀 Deployment Options

### Option 1: Instant 10-Second Deploy (Netlify Drop)
1. Go to [https://app.netlify.com/drop](https://app.netlify.com/drop) in your browser.
2. Drag and drop this folder (`SmartStorageBinWeb`) into the page.
3. You will immediately receive a permanent public HTTPS URL.

### Option 2: GitHub Pages
1. Create a repository on GitHub (e.g. `smart-storage-bin-web`).
2. Run:
   ```bash
   git remote add origin https://github.com/<YOUR_USERNAME>/smart-storage-bin-web.git
   git push -u origin master
   ```
3. Enable GitHub Pages under **Settings ➔ Pages ➔ Branch: master ➔ Save**.

### Option 3: Local Execution
- Double click `start_preview.bat` (or run `python -m http.server 8085`).
- Open `http://localhost:8085`.
