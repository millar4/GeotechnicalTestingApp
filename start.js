// startForeground.js

const { spawn, exec } = require("child_process");
const net = require("net");

// 1) Wait for a port to be open
function waitForPort(port, host = "localhost", timeout = 1000) {
  return new Promise((resolve) => {
    const tryConnect = () => {
      const socket = new net.Socket();
      socket.setTimeout(timeout);

      socket.on("error", () => {
        socket.destroy();
        setTimeout(tryConnect, timeout);
      });

      socket.on("timeout", () => {
        socket.destroy();
        setTimeout(tryConnect, timeout);
      });

      socket.connect(port, host, () => {
        socket.end();
        resolve();
      });
    };
    tryConnect();
  });
}

// 2) Poll /api/stop/status every 5 seconds
//    If we see "true", run `docker compose stop`.
function startWatchingStopRequests() {
  console.log("Starting watch loop for /api/stop/status every 5 seconds...");

  const intervalId = setInterval(async () => {
    try {
      const res = await fetch("http://localhost:8080/api/stop/status");
      if (!res.ok) {
        throw new Error(`HTTP ${res.status}`);
      }
      const text = (await res.text()).trim();
      console.log(`[stop/status poll] => "${text}"`);

      if (text === "true") {
        console.log("Stop requested! Running docker compose stop...");
        clearInterval(intervalId); // Stop polling

        exec("docker compose stop", (err, stdout, stderr) => {
          if (err) {
            console.error("Error stopping containers:", err);
          } else {
            console.log(stdout);
            if (stderr) console.error(stderr);
            console.log("Containers stopped via /api/stop/status = true");
          }
          // We expect upProc to exit automatically once containers stop.
          // We'll let the main 'upProc' exit event handle final process.exit(0).
        });
      }
    } catch (error) {
      console.error("[stop/status poll] Error:", error.message);
    }
  }, 5000);
}

// 3) Main flow: run up --build in the foreground, wait for ports, open browser, watch /api/stop/status
async function main() {
  console.log("Launching 'docker compose up --build' in the foreground...");

  // a) Spawn upProc, passing logs directly to our terminal via { stdio: 'inherit' }
  const upProc = spawn("docker", ["compose", "up", "--build"], {
    stdio: "inherit",
  });

  // b) In parallel, wait for 3100 & 8080, then open the browser
  console.log("In parallel, waiting for ports 3100 & 8080 to become available...");

  Promise.all([waitForPort(3100), waitForPort(8080)])
    .then(async () => {
      console.log("Ports 3100 and 8080 are open! Opening browser...");

      try {
        const openModule = await import("open");
        const open = openModule.default;
        await open("http://localhost:3100");
      } catch (err) {
        console.error("Failed to open browser:", err);
      }

      // Also start polling /api/stop/status
      startWatchingStopRequests();
    })
    .catch((err) => {
      console.error("Error waiting for ports:", err);
    });

  // c) Once upProc ends, we exit too (since containers are no longer running).
  upProc.on("exit", (code) => {
    console.log(`\n'docker compose up --build' has exited with code ${code}.`);
    console.log("Script will now exit.");
    process.exit(code ?? 0);
  });

  upProc.on("error", (err) => {
    console.error("Failed to run docker compose up --build:", err);
    process.exit(1);
  });
}

main();
