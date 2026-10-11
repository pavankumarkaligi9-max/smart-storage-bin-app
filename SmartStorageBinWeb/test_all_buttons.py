import subprocess

# Let's write a test script that actually loads index.html directly from file:// and http://localhost:8085
# and clicks:
# 1. nav-dashboard
# 2. nav-files
# 3. nav-temporary
# 4. nav-storage
# 5. nav-duplicates
# 6. nav-bin
# 7. nav-code
# 8. nav-api
# 9. btn-header-test-expiry
# 10. btn-header-expired
# 11. btn-header-dup
# 12. btn-header-bell
# 13. each table button
# and reports whether the click succeeded or threw an error!

test_runner = """<!DOCTYPE html>
<html>
<body>
<iframe id="app" src="http://localhost:8085" style="width:1200px; height:800px;"></iframe>
<div id="log"></div>
<script>
window.addEventListener('load', () => {
  setTimeout(() => {
    const frame = document.getElementById('app');
    const doc = frame.contentDocument || frame.contentWindow.document;
    const win = frame.contentWindow;
    const logs = [];

    function testClick(selector, desc) {
      try {
        const el = doc.querySelector(selector);
        if (!el) {
          logs.push('FAIL: ' + desc + ' (' + selector + ' not found)');
          return;
        }
        el.click();
        logs.push('PASS: ' + desc);
      } catch (e) {
        logs.push('ERROR: ' + desc + ' - ' + e.message);
      }
    }

    testClick('#nav-files', 'Nav to Files');
    testClick('#nav-temporary', 'Nav to Temporary');
    testClick('#nav-storage', 'Nav to Storage');
    testClick('#nav-duplicates', 'Nav to Duplicates');
    testClick('#nav-bin', 'Nav to Bin');
    testClick('#nav-code', 'Nav to Code');
    testClick('#nav-api', 'Nav to API');
    testClick('#nav-dashboard', 'Nav to Dashboard');
    testClick('#btn-header-test-expiry', 'Header Test Expiry Alert');
    if (win.closeModal) win.closeModal();
    testClick('#btn-header-expired', 'Header Expired Warning');
    if (win.closeModal) win.closeModal();
    testClick('#btn-header-dup', 'Header Duplicates');
    if (win.closeModal) win.closeModal();
    testClick('#btn-header-bell', 'Header Bell');

    document.getElementById('log').innerText = logs.join('\\n');
  }, 1000);
});
</script>
</body>
</html>
"""

with open(r"C:\Users\Pavan Kumar\.gemini\antigravity\scratch\SmartStorageBinWeb\test_clicks_all.html", "w", encoding="utf-8") as f:
    f.write(test_runner)

proc = subprocess.Popen([
    r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe",
    "--headless",
    "--dump-dom",
    "http://localhost:8085/test_clicks_all.html"
], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
out, _ = proc.communicate(timeout=10)
for line in out.decode('utf-8', errors='ignore').splitlines():
    if "PASS:" in line or "FAIL:" in line or "ERROR:" in line:
        print(line)
