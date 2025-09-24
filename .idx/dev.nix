{ pkgs, ... }: {
  channel = "stable-24.05";

  packages = [
    pkgs.jdk17
    pkgs.gradle
    pkgs.maven
  ];

  # Keep global env clean (no JAVA_TOOL_OPTIONS here).
  env = {
    JAVA_HOME = "${pkgs.jdk17}/lib/openjdk";
    SPRING_PROFILES_ACTIVE = "dev";
  };

  idx = {
    extensions = [
      "vscjava.vscode-java-pack"
      "google.gemini-cli-vscode-ide-companion"
    ];

    workspace = {
      onCreate = {
        # Try to set execute bits; fall back to running via bash if still not executable.
        install = ''
          (chmod +x ./gradlew 2>/dev/null || true)
          if [ -x ./gradlew ]; then
            ./gradlew build --no-daemon
          else
            bash ./gradlew build --no-daemon
          fi
        '';
      };
      onStart = {
        # Short, non-daemon checks only (don't run your server here)
        verify-java = "java -version";
        default.openFiles = [ "src/main/AiChatServiceApplication.java" ];
      };
    };

    previews = {
      enable = true;

      previews = {
        # Gradle Spring Boot
        spring-gradle = {
          manager = "gradle";
          cwd = "";
          command = [ "./gradlew" "bootRun" "--no-daemon" ];
          # Safe to apply GC flags ONLY to your app:
          env = {
            SPRING_PROFILES_ACTIVE = "dev";
            JAVA_TOOL_OPTIONS = "-XX:+UseZGC -XX:+UseStringDeduplication";
          };
        };

        # Optional Maven preview
        spring-maven = {
          manager = "gradle";
          cwd = "";
          command = [ "./mvnw" "-DskipTests" "spring-boot:run" ];
          env = {
            SPRING_PROFILES_ACTIVE = "dev";
            JAVA_TOOL_OPTIONS = "-XX:+UseZGC -XX:+UseStringDeduplication";
          };
        };
      };
    };
  };
}
