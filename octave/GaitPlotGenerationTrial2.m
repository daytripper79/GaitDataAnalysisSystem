clear

delete(findall(0, 'Type', 'figure', 'FileName', []));

set(0,'DefaultAxesFontSize',14)

printf ("Octave is working... \n");

% Input file
inputFile = argv(){1};

% MAK leg
makFoot = argv(){2};

makLeft = 0;
if strcmp(makFoot,'L') == 1 
   makLeft = 1;
end 

% Retrievement of CVS files
calculatedFile = strcat(inputFile,".CSV.CALCULATED_DATA.csv");
calculatedData=csvread(calculatedFile);
gaitFileMak =  strcat(inputFile,".CSV.GAIT_DATA_MAK.csv");
gaitDataMak=csvread(gaitFileMak);
gaitFileFree =  strcat(inputFile,".CSV.GAIT_DATA_FREE.csv");
gaitDataFree=csvread(gaitFileFree);
pressureFile = strcat(inputFile,".CSV.PRESSURE_DATA_MAK.csv");
pressureDataMak=csvread(pressureFile);
pressureFile = strcat(inputFile,".CSV.PRESSURE_DATA_FREE.csv");
pressureDataFree=csvread(pressureFile);

% CALCULATED Data
calculatedDataTime = calculatedData(2:end, 1);
calculatedDataPositionAngle = calculatedData(2:end, 2);
calculatedDataEulerX = calculatedData(2:end, 3);
calculatedDataEulerY = calculatedData(2:end, 4);
calculatedDataEulerZ = calculatedData(2:end, 5);
calculatedDataHalluxMak = calculatedData(2:end, 6);
calculatedDataToesMak = calculatedData(2:end, 7);
calculatedDataMetaExtMak = calculatedData(2:end, 8);
calculatedDataMetaIntermMak = calculatedData(2:end, 9);
calculatedDataMetaIntMak = calculatedData(2:end, 10);
calculatedDataArchMak = calculatedData(2:end, 11);
calculatedDataHeelExtMak = calculatedData(2:end, 12);
calculatedDataHeelIntMak = calculatedData(2:end, 13);
calculatedDataTreadMak = calculatedData(2:end, 14);
calculatedDataStepMak = calculatedData(2:end, 15);
calculatedDataCopXMak = calculatedData(2:end, 16);
calculatedDataCopYMak = calculatedData(2:end, 17);
calculatedDataRelativeCopXMak = calculatedData(2:end, 18);
calculatedDataRelativeCopYMak = calculatedData(2:end, 19);
calculatedDataMeanRelativeCopXMak = calculatedData(2:end, 20);
calculatedDataMeanRelativeCopYMak = calculatedData(2:end, 21);

calculatedDataHalluxFree = calculatedData(2:end, 22);
calculatedDataToesFree = calculatedData(2:end, 23);
calculatedDataMetaExtFree = calculatedData(2:end, 24);
calculatedDataMetaIntermFree = calculatedData(2:end, 25);
calculatedDataMetaIntFree = calculatedData(2:end, 26);
calculatedDataArchFree = calculatedData(2:end, 27);
calculatedDataHeelExtFree = calculatedData(2:end, 28);
calculatedDataHeelIntFree = calculatedData(2:end, 29);
calculatedDataTreadFree = calculatedData(2:end, 30);
calculatedDataStepFree = calculatedData(2:end, 31);
calculatedDataCopXFree = calculatedData(2:end, 32);
calculatedDataCopYFree = calculatedData(2:end, 33);
calculatedDataRelativeCopXFree = calculatedData(2:end, 34);
calculatedDataRelativeCopYFree = calculatedData(2:end, 35);
calculatedDataMeanRelativeCopXFree = calculatedData(2:end, 36);
calculatedDataMeanRelativeCopYFree = calculatedData(2:end, 37);


% GAIT Data columns
gaitDataStepMak = gaitDataMak(2:end, 1);
[ground gaitInitialPhaseMak ground ground ground ground ground ground ground ground ground ground] = textread(gaitFileMak, '%d %s %d %d %d %d %d %d %d %d %d %d', 'delimiter', ',');
gaitInitialTimeMak = gaitDataMak(2:end, 3);
gaitDataFinalTimeMak = gaitDataMak(2:end, 4);
gaitDataTotalTimeMak = gaitDataMak(2:end, 5);
gaitDataStepTimeMak = gaitDataMak(2:end, 6);
gaitDataPercentTimeMak = gaitDataMak(2:end, 7);
gaitDataTotalGaitMak = gaitDataMak(2:end, 8);

gaitDataStepFree = gaitDataFree(2:end, 1);
[free gaitInitialPhaseFree free free free free free free free free free free] = textread(gaitFileFree, '%d %s %d %d %d %d %d %d %d %d %d %d', 'delimiter', ',');
gaitDataInitialTimeFree = gaitDataFree(2:end, 3);
gaitDataFinalTimeFree = gaitDataFree(2:end, 4);
gaitDataTotalTimeFree = gaitDataFree(2:end, 5);
gaitDataStepTimeFree = gaitDataFree(2:end, 6);
gaitDataPercentTimeFree = gaitDataFree(2:end, 7);
gaitDataTotalGaitFree = gaitDataFree(2:end, 8);

% PRESSURE Data columns
pressureDataStepMak = pressureDataMak(2:(end), 1);
pressureDataHalluxMak = pressureDataMak(2:(end), 2);
pressureDataHalluxTotalMak = pressureDataMak(2:(end), 3);
pressureDataHalluxRelativeMak = pressureDataMak(2:(end), 4);
pressureDataToesMak = pressureDataMak(2:(end), 5);
pressureDataToesTotalMak = pressureDataMak(2:(end), 6);
pressureDataToesRelativeMak = pressureDataMak(2:(end), 7);
pressureDataMetaExtMak = pressureDataMak(2:(end), 8);
pressureDataMetaExtTotalMak = pressureDataMak(2:(end), 9);
pressureDataMetaExtRelativeMak = pressureDataMak(2:(end), 10);
pressureDataMetaIntermMak = pressureDataMak(2:(end), 11);
pressureDataMetaIntermTotalMak = pressureDataMak(2:(end), 12);
pressureDataMetaIntermRelativeMak = pressureDataMak(2:(end), 13);
pressureDataMetaIntMak = pressureDataMak(2:(end), 14);
pressureDataMetaIntTotalMak = pressureDataMak(2:(end), 15);
pressureDataMetaIntRelativeMak = pressureDataMak(2:(end), 16);
pressureDataArchMak = pressureDataMak(2:(end), 17);
pressureDataArchTotalMak = pressureDataMak(2:(end), 18);
pressureDataArchRelativeMak = pressureDataMak(2:(end), 19);
pressureDataHeelIntMak = pressureDataMak(2:(end), 20);
pressureDataHeelIntTotalMak = pressureDataMak(2:(end), 21);
pressureDataHeelIntRelativeMak = pressureDataMak(2:(end), 22);
pressureDataHeelExtMak = pressureDataMak(2:(end), 23);
pressureDataHeelExtTotalMak = pressureDataMak(2:(end), 24);
pressureDataHeelExtRelativeMak = pressureDataMak(2:(end), 25);

pressureDataStepFree = pressureDataFree(2:(end), 1);
pressureDataHalluxFree = pressureDataFree(2:(end), 2);
pressureDataHalluxTotalFree = pressureDataFree(2:(end), 3);
pressureDataHalluxRelativeFree = pressureDataFree(2:(end), 4);
pressureDataToesFree = pressureDataFree(2:(end), 5);
pressureDataToesTotalFree = pressureDataFree(2:(end), 6);
pressureDataToesRelativeFree = pressureDataFree(2:(end), 7);
pressureDataMetaExtFree = pressureDataFree(2:(end), 8);
pressureDataMetaExtTotalFree = pressureDataFree(2:(end), 9);
pressureDataMetaExtRelativeFree = pressureDataFree(2:(end), 10);
pressureDataMetaIntermFree = pressureDataFree(2:(end), 11);
pressureDataMetaIntermTotalFree = pressureDataFree(2:(end), 12);
pressureDataMetaIntermRelativeFree = pressureDataFree(2:(end), 13);
pressureDataMetaIntFree = pressureDataFree(2:(end), 14);
pressureDataMetaIntTotalFree = pressureDataFree(2:(end), 15);
pressureDataMetaIntRelativeFree = pressureDataFree(2:(end), 16);
pressureDataArchFree = pressureDataFree(2:(end), 17);
pressureDataArchTotalFree = pressureDataFree(2:(end), 18);
pressureDataArchRelativeFree = pressureDataFree(2:(end), 19);
pressureDataHeelIntFree = pressureDataFree(2:(end), 20);
pressureDataHeelIntTotalFree = pressureDataFree(2:(end), 21);
pressureDataHeelIntRelativeFree = pressureDataFree(2:(end), 22);
pressureDataHeelExtFree = pressureDataFree(2:(end), 23);
pressureDataHeelExtTotalFree = pressureDataFree(2:(end), 24);
pressureDataHeelExtRelativeFree = pressureDataFree(2:(end), 25);



% Set NaN to 0 values as they dont be represented in plots
calculatedDataCopXMak(calculatedDataCopXMak==0) = nan;
calculatedDataCopYMak(calculatedDataCopYMak==0) = nan;
calculatedDataRelativeCopXMak(calculatedDataRelativeCopXMak==0) = nan;
calculatedDataRelativeCopYMak(calculatedDataRelativeCopYMak==0) = nan;
calculatedDataMeanRelativeCopXMak(calculatedDataMeanRelativeCopXMak==0) = nan;
calculatedDataMeanRelativeCopYMak(calculatedDataMeanRelativeCopYMak==0) = nan;

calculatedDataCopXFree(calculatedDataCopXFree==0) = nan;
calculatedDataCopYFree(calculatedDataCopYFree==0) = nan;
calculatedDataRelativeCopXFree(calculatedDataRelativeCopXFree==0) = nan;
calculatedDataRelativeCopYFree(calculatedDataRelativeCopYFree==0) = nan;
calculatedDataMeanRelativeCopXFree(calculatedDataMeanRelativeCopXFree==0) = nan;
calculatedDataMeanRelativeCopYFree(calculatedDataMeanRelativeCopYFree==0) = nan;

% CONSTANTS
imgFoot = imread(path);
insoleXMak =  max(calculatedDataCopXMak) + 15
insoleYMak =  max(calculatedDataCopYMak) + 28
insoleXFree =  max(calculatedDataCopXFree) + 15
insoleYFree =  max(calculatedDataCopYFree) + 28
footPosX = {'ext.' ; '' ; 'int.'};
footPosY = {'' ;'hindfoot' ; '' ;'middlefoot'; ''; 'forefoot'};

initialTime = calculateDataTime(1);
finalTime = calculateDataTime(end);
initialIndex = find(calculatedDataTime == initialTime, 1, 'last')
finalIndex =   find(calculatedDataTime == finalTime, 1, 'last')

% PLOT POSITION ANGLE
figure 
hold on
title('Position Angle')
ylabel("Position angle (C)");
xlabel("Time(s)"); 
plot(calculatedDataTime, calculatedDataPositionAngle,'LineWidth',1,'color',[0.01 0.56 0.99]);
xlim([initialTime finalTime])
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
line(xlim(), [max(calculatedDataPositionAngle(initialIndex:finalIndex)) max(calculatedDataPositionAngle(initialIndex:finalIndex))], "linestyle", "--", "color", [0.85 0.078 0.23]);
line(xlim(), [min(calculatedDataPositionAngle(initialIndex:finalIndex)) min(calculatedDataPositionAngle(initialIndex:finalIndex))], "linestyle", "--", "color", [0.85 0.078 0.23]);
for i = calculatedDataStepMak(initialIndex):calculatedDataStepMak(finalIndex)
  if strcmp(gaitInitialPhaseMak(i,1),'STANCE') == 1 
    line([gaitInitialTimeMak(i) gaitInitialTimeMak(i)], [ylim()], "linestyle", "--", "color", [0.41 0.41 0.41]);
  end 
end
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
hgsave(gcf,strcat(inputFile,"position.ofig"))
print(gcf,strcat(inputFile,"position.png"))

hold off

% PLOT EULER X
figure 
hold on
plot(calculatedDataTime, calculatedDataEulerX,'LineWidth',1,'color', [0.19 0.80 0.19])
title('Lateral displacement of hip')
ylabel("Degrees");
xlabel("Time(s)"); 
xlim([initialTime finalTime])
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
line(xlim(), [max(calculatedDataEulerX(initialIndex:finalIndex)) max(calculatedDataEulerX(initialIndex:finalIndex))], "linestyle", "--", "color",  [0.85 0.078 0.23]);
line(xlim(), [min(calculatedDataEulerX(initialIndex:finalIndex)) min(calculatedDataEulerX(initialIndex:finalIndex))], "linestyle", "--", "color",  [0.85 0.078 0.23]);
for i = calculatedDataStepMak(initialIndex):calculatedDataStepMak(finalIndex)
  if strcmp(gaitInitialPhaseMak(i,1),'STANCE') == 1 
    line([gaitInitialTimeMak(i) gaitInitialTimeMak(i)], [ylim()], "linestyle", "--", "color", [0.41 0.41 0.41]);
  end 
end

set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
hold off
hgsave(gcf,strcat(inputFile,"eulerx.ofig"))
print(gcf,strcat(inputFile,"eulerx.png"))

% PLOT EULER Y
figure 
hold on
title('Forward-Backward Displacement of hip')
plot(calculatedDataTime, calculatedDataEulerY, 'LineWidth',1, 'color',[0.99 0.54 0])
xlabel("Time(s)"); 
xlim([initialTime finalTime])
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
line(xlim(), [max(calculatedDataEulerY(initialIndex:finalIndex)) max(calculatedDataEulerY(initialIndex:finalIndex))], "linestyle", "--", "color", [0.85 0.078 0.23]);
line(xlim(), [min(calculatedDataEulerY(initialIndex:finalIndex)) min(calculatedDataEulerY(initialIndex:finalIndex))], "linestyle", "--", "color", [0.85 0.078 0.23]);
for i = calculatedDataStepMak(initialIndex):calculatedDataStepMak(finalIndex)
  if strcmp(gaitInitialPhaseMak(i,1),'STANCE') == 1 
    line([gaitInitialTimeMak(i) gaitInitialTimeMak(i)], [ylim()], "linestyle", ":", "color", [0.41 0.41 0.41]);
  end 
end
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
hold off
hgsave(gcf,strcat(inputFile,"eulery.ofig"))
print(gcf,strcat(inputFile,"eulery.png"))

% PLOT INSOLE MAK
figure
hold on
plot(calculatedDataTime, calculatedDataHalluxMak, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataMetaExtMak, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataMetaIntMak, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataArchMak,'LineWidth',2)
plot(calculatedDataTime, calculatedDataHeelExtMak, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataHeelIntMak, 'LineWidth',2, 'color',[0.99 0.78 0.57])
title('Insole pressures by time in MAK')
ylabel("Insole sensors (millibar)");
xlabel("Time(s)"); 
for i = 2:length(gaitInitialTimeMak)
  if strcmp(gaitInitialPhaseMak(i,1),'STANCE') == 1 
    line([gaitInitialTimeMak(i) gaitInitialTimeMak(i)], [ylim()], "linestyle", ":", "color", "k");
  end 
end
xlim([initialTime finalTime])
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
hgsave(gcf,strcat(inputFile,"gait.ofig"))
legend({'Hallux', 'Met Ext', 'Met Int', 'Arch', 'Heel Ext', 'Heel Int'},'FontSize',10,'Location','southoutside', 'Orientation','horizontal')
print(gcf,strcat(inputFile,"gait.png"))
hold off;

% PLOT INSOLE FREE
figure
hold on
plot(calculatedDataTime, calculatedDataHalluxFree, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataMetaExtFree, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataMetaIntFree, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataArchFree, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataHeelExtFree, 'LineWidth',2)
plot(calculatedDataTime, calculatedDataHeelIntFree, 'LineWidth',2, 'color',[0.99 0.78 0.57])
title('Insole pressures by time in Free')
ylabel("Insole sensors (millibar)");
xlabel("Time(s)"); 
for i = 2:length(gaitDataInitialTimeFree)
  if strcmp(gaitInitialPhaseFree(i,1),'STANCE') == 1 
    line([gaitDataInitialTimeFree(i) gaitDataInitialTimeFree(i)], [ylim()], "linestyle", ":", "color", "k");
  end 
end
xlim([initialTime finalTime])
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
hgsave(gcf,strcat(inputFile,"gaitfree.ofig"))
legend({'Hallux', 'Met Ext', 'Met Int', 'Arch', 'Heel Ext', 'Heel Int'},'Location','northeastoutside')
print(gcf,strcat(inputFile,"gaitfree.png"))
hold off

% PLOT TOTAL Pressures in sensors by step
figure
ax1 = subplot(2,1,1);
hold on
bar(pressureDataStepMak, [pressureDataHalluxTotalMak  pressureDataMetaExtTotalMak ...
                          pressureDataMetaIntTotalMak pressureDataArchTotalMak    ...
                          pressureDataHeelIntTotalMak pressureDataHeelExtTotalMak] )
title('Total pressure in sensors by step MAK')
legend({'Hallux', 'Met Ext', 'Met Int', 'Arch', 'Heel Int', 'Heel Ext'},'Location','northeastoutside')
xlabel("Steps"); 
ylabel("Sensor's Total pressure(mb)"); 
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
ax2 = subplot(2,1,2);
bar(pressureDataStepFree, [pressureDataHalluxTotalFree pressureDataMetaExtTotalFree ...
                           pressureDataMetaIntermTotalFree pressureDataArchTotalFree ...
                           pressureDataHeelIntTotalFree pressureDataHeelExtTotalFree] )
title('Total pressure in sensors by step Free')
legend({'Hallux', 'Met Ext', 'Met Int', 'Arch', 'Heel Int', 'Heel Ext'},'Location','northeastoutside')
xlabel("Steps"); 
ylabel("Sensor's Total pressure(mb)"); 
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
hold off
hgsave(gcf,strcat(inputFile,"pressuretotal.ofig"))
print(gcf,strcat(inputFile,"pressuretotal.png"))

% PLOT MAX Pressures in sensors by step
figure
ax1 = subplot(2,1,1);
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
hold on
bar(pressureDataStepMak, [pressureDataHalluxMak  pressureDataMetaExtMak ...
                          pressureDataMetaIntMak pressureDataArchMak    ...
                          pressureDataHeelIntMak pressureDataHeelExtMak] )
title(' pressure in sensors by step MAK')
legend({'Hallux', 'Met Ext', 'Met Int', 'Arch', 'Heel Int', 'Heel Ext'},'Location','northeastoutside')
xlabel("Steps"); 
ylabel("Sensor's  pressure(mb)"); 
set(gca,'xtick',[])
set(gca,'yminorgrid','on')
xlim([calculatedDataStepMak(initialIndex) calculatedDataStepMak(finalIndex)])
ax2 = subplot(2,1,2);
bar(pressureDataStepFree, [pressureDataHalluxFree pressureDataMetaExtFree ...
                           pressureDataMetaIntermFree pressureDataArchFree ...
                           pressureDataHeelIntFree pressureDataHeelExtFree] )
title('Max pressure in sensors by step Free')
legend({'Hallux', 'Met Ext', 'Met Int', 'Arch', 'Heel Int', 'Heel Ext'},'Location','northeastoutside')
xlabel("Steps"); 
ylabel("Max Sensor's  pressure(mb)"); 
set(gca,'xtick',[])
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
xlim([calculatedDataStepMak(initialIndex) calculatedDataStepMak(finalIndex)])
hold off
hgsave(gcf,strcat(inputFile,"pressuremax.ofig"))
print(gcf,strcat(inputFile,"pressuremax.png"))


% PLOT RELATIVE Pressures in sensors by step
figure
ax1 = subplot(2,1,1);
hold on
bar(pressureDataStepMak, [pressureDataHalluxRelativeMak  pressureDataMetaExtRelativeMak ...
                          pressureDataMetaIntRelativeMak pressureDataArchRelativeMak    ...
                          pressureDataHeelIntRelativeMak pressureDataHeelExtRelativeMak] )
title('Relative pressure in sensors by step MAK')
legend({'Hallux', 'Met Ext', 'Met Int', 'Arch', 'Heel Int', 'Heel Ext'},'Location','northeastoutside')
xlabel("Steps"); 
ylabel("Sensor's Relative pressure(mb)"); 
set(gca,'xtick',[])
set(gca,'yminorgrid','on')
xlim([calculatedDataStepMak(initialIndex) calculatedDataStepMak(finalIndex)])
ax2 = subplot(2,1,2);
bar(pressureDataStepFree, [pressureDataHalluxRelativeFree pressureDataMetaExtRelativeFree ...
                           pressureDataMetaIntermRelativeFree pressureDataArchRelativeFree ...
                           pressureDataHeelIntRelativeFree pressureDataHeelExtRelativeFree] )
title('Relative pressure in sensors by step Free')
legend({'Hallux', 'Met Ext', 'Met Int', 'Arch', 'Heel Int', 'Heel Ext'},'Location','northeastoutside')
xlabel("Steps"); 
ylabel("Sensor's Relative pressure(mb)"); 
set(gca,'XMinorTick','on','YMinorTick','on')
set(gca,'yminorgrid','on')
hold off
hgsave(gcf,strcat(inputFile,"pressurerel.ofig"))
print(gcf,strcat(inputFile,"pressurerel.png"))


% PLOT COP X MAK(%)
figure
ax1 = subplot(2,1,1);
hold on
plot(calculatedDataTime, calculatedDataCopXMak, 'LineWidth',1,'color',[0.41 0.35 0.80]);
title('Cop X position by Time Mak')
xlabel("Time(s)"); 
ylabel("CoP X position"); 
xlim([initialTime finalTime])
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)] ,'xticklabel')
ylim([0 insoleXMak])
range = insoleXMak/2;
set(gca, 'ytick', [0 range insoleXMak] ,'yticklabel',footPosX)
grid on
hold off
    
% PLOT COP Y MAK(%)
ax2 = subplot(2,1,2);
hold on
plot(calculatedDataTime, calculatedDataCopYMak, 'LineWidth',1,'color',[0.99 0.83 0]);
title('Cop Y position by Time Mak')
xlabel("Time(s)"); 
ylabel("CoP Y position"); 
xlim([initialTime finalTime])
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
ylim([0 insoleYMak])
set(gca, 'yticklabel',footPosY)
ylim([0 insoleYMak])
range = insoleYMak/6
set(gca, 'ytick', 0:range:insoleYMak ,'yticklabel',footPosY)
grid on
hgsave(gcf,strcat(inputFile,"cop.ofig"))
print(gcf,strcat(inputFile,"cop.png"))

hold off

% PLOT COP X FREE(%)
figure
hold on
ax1 = subplot(2,1,1);
plot(calculatedDataTime, calculatedDataCopXFree, 'LineWidth',1,'color',[0.41 0.35 0.80]);
hold on
title('Cop X position by Time Free')
xlabel("Time(s)"); 
ylabel("CoP X position"); 
xlim([initialTime finalTime])
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
ylim([0 insoleXFree])
range = insoleXFree/2;
set(gca, 'ytick', [0 range insoleXFree] ,'yticklabel',footPosX)
grid on
hold off

% PLOT COP Y FREE(%)
hold on
ax2 = subplot(2,1,2);
plot(calculatedDataTime, calculatedDataCopYFree, 'LineWidth',1,'color',[0.99 0.83 0]);
title('Cop Y position by Time Free')
xlabel("Time(s)"); 
ylabel("CoP Y posotion"); 
xlim([initialTime finalTime])
set(gca, 'xtick', [calculatedDataTime(initialIndex):1:calculatedDataTime(finalIndex)])
ylim([0 insoleYFree])
range = insoleYFree/6
set(gca, 'ytick', 0:range:insoleYFree ,'yticklabel',footPosY)
grid on
hgsave(gcf,strcat(inputFile,"copfree.ofig"))
print(gcf,strcat(inputFile,"copfree.png"))

hold off

% PLOT Isolated Steps MAK
if(length(gaitDataStepMak) ~= 0)          
if strcmp(gaitInitialPhaseMak(2,1),'STANCE') == 1 
  initial = gaitDataStepMak(1);
else
  initial = gaitDataStepMak(2);
end 
  
  
for i = gaitDataStepMak(1):2:gaitDataStepMak(length(gaitDataStepMak))
  
  initialIndex = find(calculatedDataStepMak == i, 1, 'first');
  finalIndex =   find(calculatedDataStepMak == (i+1), 1, 'first');
  interval =  calculatedDataTime(finalIndex) - calculatedDataTime(initialIndex);
  rangeInterval = interval/2;
  a = sprintf('%d',0);
  b = sprintf('%d',rangeInterval);
  c = sprintf('%d',interval);
  labels = [ a ; b ; c];
  
  figure
  % PLOT Isolated Step CoP
  hold on
  str = sprintf('Cop trajectory %d', i);
  title(str)
  axis equal
  xlabel("CoP X")   
  ylabel("CoP Y") 

  if makLeft == 0
       set(gca, 'XDir', 'reverse')
  end 
  
  x = calculatedDataCopXMak(initialIndex:finalIndex);
  xlim([0 insoleXMak])
  range = insoleXMak/2
  set(gca, 'xtick', [0 range insoleXMak] ,'xticklabel',footPosX)
  y = calculatedDataCopYMak(initialIndex:finalIndex);
  ylim([0 insoleYMak])
  range = insoleYMak/6
  set(gca, 'ytick', 0:range:insoleYMak ,'yticklabel',footPosY)
  z = calculatedDataTime(initialIndex:finalIndex);  
  surf([x(:) x(:)], [y(:) y(:)], [z(:) z(:)], 'FaceColor', 'none', 'EdgeColor', 'interp', 'LineWidth', 3)
  view(2)
  cbh = colorbar;
  set(cbh,'YTick', [], 'YLabel', 'Time progression')
  grid on
  hold off
  hgsave(gcf,strcat(inputFile,"stepcop-", str, ".ofig"))
  str = sprintf('%d', i);
  print(gcf,strcat(inputFile,"stepcop-", str, ".png"))

  
  figure
  % PLOT Isolated Step 
  hold on
  str = sprintf('Insole sensors progression by time for a single step %d', i);
  title(str)
  interval =  calculatedDataTime(finalIndex) - calculatedDataTime(initialIndex);
  rangeInterval = interval/2;
  set(gca, 'xtick', [calculatedDataTime(initialIndex):rangeInterval:calculatedDataTime(finalIndex)] ,'xticklabel', labels)
  xlim([calculatedDataTime(initialIndex) calculatedDataTime(finalIndex)])
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataHalluxMak(initialIndex:finalIndex),'LineWidth',2)  
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataMetaExtMak(initialIndex:finalIndex),'LineWidth',2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataMetaIntMak(initialIndex:finalIndex),'LineWidth',2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataArchMak(initialIndex:finalIndex),'LineWidth',2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataHeelExtMak(initialIndex:finalIndex),'LineWidth',2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataHeelIntMak(initialIndex:finalIndex),'LineWidth',2, 'color',[0.99 0.78 0.57])
  ylabel(ax2,"Insole sensors (millibar)");
  xlabel(ax2,"Time(s)"); 
  grid on
  hold off
  str = sprintf('%d', i);
  hgsave(gcf,strcat(inputFile,"step-", str, ".ofig"))
  legend({'Hallux', 'Met Ext', 'Met Int', 'Arch', 'Heel Int', 'Heel Ext'},'Location','southoutside', 'Orientation','horizontal')
  print(gcf,strcat(inputFile,"step-", str, ".png"))

  
end
  
end

% PLOT Isolated Steps FREE
if(length(gaitDataStepFree) ~= 0) 
if strcmp(gaitInitialPhaseFree(2,1),'STANCE') == 1 
  initial = gaitDataStepFree(1);
else
  initial = gaitDataStepFree(2);
end 
for i = initial:2:gaitDataStepFree(length(gaitDataStepFree))
    
  initialIndex = find(calculatedDataStepFree == i, 1, 'first');
  finalIndex =   find(calculatedDataStepFree == i, 1, 'last');   
  interval =  calculatedDataTime(finalIndex) - calculatedDataTime(initialIndex);
  rangeInterval = interval/2;
  a = sprintf('%d',0);
  b = sprintf('%d',rangeInterval);
  c = sprintf('%d',interval);
  labels = [ a ; b ; c];
  
  figure
  % PLOT Isolated Step 
  hold on
  str = sprintf('Cop trajectory %d', i);
  title(str)
  axis equal  
  xlabel(ax1, "CoP X Free")   
  ylabel(ax1, "CoP Y Free") 
  
  if makLeft == 1
       set(gca, 'XDir', 'reverse')
  end 
  
  x = calculatedDataCopXFree(initialIndex:finalIndex);
  xlim([0 insoleXFree])
  range = insoleXFree/2
  set(gca, 'xtick', [0 range insoleXFree] ,'xticklabel',footPosX)
  y = calculatedDataCopYFree(initialIndex:finalIndex);
  ylim([0 insoleYFree])
  range = insoleYFree/6
  set(gca, 'ytick', 0:range:insoleYFree ,'yticklabel',footPosY)
  z = calculatedDataTime(initialIndex:finalIndex);  surf([x(:) x(:)], [y(:) y(:)], [z(:) z(:)], 'FaceColor', 'none', 'EdgeColor', 'interp', 'LineWidth', 3);            
  view(2) 
  cbh = colorbar;
  set(cbh,'YTick', [], 'YLabel', 'Time progression')
  grid on
  hold off
  str = sprintf('%d', i);
  hgsave(gcf,strcat(inputFile,"stepcopfree-", str, ".ofig"))
  print(gcf,strcat(inputFile,"stepcopfree-", str, ".png"))

  figure
  % PLOT Isolated Step
  hold on
  str = sprintf('Insole sensor Isolated Step Free %d', i);
  str = sprintf('Insole sensors progression by time for a single step');
  title(str)
  set(gca, 'xtick', [calculatedDataTime(initialIndex):rangeInterval:calculatedDataTime(finalIndex)] ,'xticklabel', labels)
  xlim([calculatedDataTime(initialIndex) calculatedDataTime(finalIndex)])
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataHalluxFree(initialIndex:finalIndex),'LineWidth',2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataMetaExtFree(initialIndex:finalIndex),'LineWidth',2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataMetaIntFree(initialIndex:finalIndex),'LineWidth',2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataArchFree(initialIndex:finalIndex),'LineWidth',2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataHeelExtFree(initialIndex:finalIndex),'LineWidth',2)
  plot(calculatedDataTime(initialIndex:finalIndex), calculatedDataHeelIntFree(initialIndex:finalIndex),'LineWidth',2, 'color',[0.99 0.78 0.57])
  ylabel(ax2,"Insole sensors (millibar)");
  xlabel(ax2,"Time(s)"); 
  grid on
  str = sprintf('%d', i);
  hgsave(gcf,strcat(inputFile,"stepfree-", str, ".ofig"))
  legend({'Hallux', 'Met Ext', 'Met Int', 'Arch', 'Heel Ext', 'Heel Int'},'FontSize',10,'Location','southoutside', 'Orientation','horizontal')
  hold off
  print(gcf,strcat(inputFile,"stepfree-", str, ".png"))
  
end
end

